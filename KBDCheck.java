import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/*
 * Ma_Sys.ma Keyboard Check Application 1.0.1, Copyright (c) 2019 Ma_Sys.ma.
 * For further info send an e-mail to Ma_Sys.ma@web.de
 */
public class KBDCheck extends JFrame {

	private static final float DIV_NS_TO_MS = 1000000.0f;

	private static enum KeyChange { PRESS, UP, DOWN }

	private static class RegisteredEvent {

		KeyChange kc;
		long t;
		KeyEvent ev;

		long deltaT;

		RegisteredEvent(KeyChange kc, KeyEvent ev) {
			this.kc = kc;
			this.ev = ev;
			this.t = System.nanoTime();
			deltaT = -1;
		}

	}

	private RegisteredEvent[] events = new RegisteredEvent[4096];
	private int eventIdx = 0;

	// large buffer size
	private final ArrayBlockingQueue<RegisteredEvent> queue =
				new ArrayBlockingQueue<RegisteredEvent>(4096);
	private final Map<Integer, RegisteredEvent> downKeys =
				new HashMap<Integer, RegisteredEvent>();

	private long lastKeypress;
	private long lastUpDown;

	private char[] buf;
	private int bufpos = 0;

	public KBDCheck() {
		super("KBDCheck 1.0.0");

		buf = "________________________________________".toCharArray();

		for(int i = 0; i < events.length; i++)
			events[i] = new RegisteredEvent(null, null);

		Container cp = getContentPane();
		cp.setLayout(new FlowLayout(FlowLayout.LEFT));
		cp.add(new JLabel("Input:"));
		JTextField f = new JTextField();
		f.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent ev) {
				try {
					events[eventIdx].t = System.nanoTime();
					events[eventIdx].kc = KeyChange.PRESS;
					events[eventIdx].ev = ev;
					queue.put(events[eventIdx]);
					// unchecked ringbuffering...
					eventIdx = (eventIdx + 1) %
								events.length;
				} catch(InterruptedException ex) {
					throw new RuntimeException(ex);
				}
			}

			@Override
			public void keyPressed(KeyEvent ev) {
				try {
					events[eventIdx].t = System.nanoTime();
					events[eventIdx].kc = KeyChange.DOWN;
					events[eventIdx].ev = ev;
					queue.put(events[eventIdx]);
					eventIdx = (eventIdx + 1) %
								events.length;
				} catch(InterruptedException ex) {
					throw new RuntimeException(ex);
				}
			}

			@Override
			public void keyReleased(KeyEvent ev) {
				try {
					events[eventIdx].t = System.nanoTime();
					events[eventIdx].kc = KeyChange.UP;
					events[eventIdx].ev = ev;
					queue.put(events[eventIdx]);
					eventIdx = (eventIdx + 1) %
								events.length;
				} catch(InterruptedException ex) {
					throw new RuntimeException(ex);
				}
			}

		});
		cp.add(f);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void start() {
		lastKeypress = System.nanoTime();
		lastUpDown = lastKeypress;

		setVisible(true);

		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					do {
						RegisteredEvent ev =
								queue.take();
						switch(ev.kc) {
						case PRESS: handlePress(ev);
						            break;
						case DOWN:  handleDown(ev);
						            break;
						case UP:    handleUp(ev);
						            break;
						}
					} while(!isInterrupted());
				} catch(InterruptedException ex) {
					System.err.println("Ignored Exception: "
							+ ex.toString());
				}
			}
		};
		t.start();
	}

	private void handlePress(RegisteredEvent ev) {
		long deltaT = ev.t - lastKeypress;
		lastKeypress = max(lastKeypress, ev.t);
		buf[bufpos] = ev.ev.getKeyChar();
		bufpos = (bufpos + 1) % buf.length;
		if(ev.ev.getKeyChar() == ' ') {
			Arrays.fill(buf, '_');
			bufpos = 0;
		}
		System.out.println(String.format(
			"PRESS; %10s;  %12.4f;;;  %s",
			String.valueOf(ev.ev.getKeyChar()),
			deltaT / DIV_NS_TO_MS,
			new String(buf)
		));
	}

	private static long max(long a, long b) {
		return a > b? a: b;
	}

	private void handleDown(RegisteredEvent ev) {
		if(downKeys.containsKey(ev.ev.getKeyCode())) {
			RegisteredEvent evo = downKeys.get(ev.ev.getKeyCode());
			System.out.println(String.format(
				"RDOWN; %10s;  %12.4f;  %12.4f;  %12.4f",
				describeKeycode(ev.ev),
				(ev.t - lastUpDown) / DIV_NS_TO_MS,
				evo.deltaT / DIV_NS_TO_MS,
				(ev.t - evo.t) / DIV_NS_TO_MS
			));
		}
		ev.deltaT = ev.t - lastUpDown;
		lastUpDown = max(lastUpDown, ev.t);
		downKeys.put(ev.ev.getKeyCode(), ev);
	}

	private void handleUp(RegisteredEvent ev) {
		long deltaT = ev.t - lastUpDown;
		lastUpDown = max(lastUpDown, ev.t);

		if(downKeys.containsKey(ev.ev.getKeyCode())) {
			RegisteredEvent evo = downKeys.get(ev.ev.getKeyCode());
			// - Zeit zwischem letzten registrierten Tastenhochgehen
			//   oder runtergehen und dem aktuellen UP-event
			//   (bspw. Abstand zwischen Druck auf Shift und Taste
			//   loslassen)
			// + Vergangenes letztes Tastenhochgehen oder
			//   runtergehen und DOWN event (minimalzeit zwischen
			//   zwei Tasten)
			// ~ Zeit zwischen dem runterdrücken der aktuellen
			//   Taste und ihrem Loslassen
			System.out.println(String.format(
				"DOWNUP;%10s;  %12.4f;  %12.4f;  %12.4f",
				describeKeycode(ev.ev),
				deltaT / DIV_NS_TO_MS,
				evo.deltaT / DIV_NS_TO_MS,
				(ev.t - evo.t) / DIV_NS_TO_MS
			));
			downKeys.remove(ev.ev.getKeyCode());
		} else {
			System.out.println(String.format(
				"EUP;   %10s;  %12.4f",
				describeKeycode(ev.ev),
				deltaT / DIV_NS_TO_MS
			));
		}
	}

	private String describeKeycode(KeyEvent ev) {
		return ev.getKeyCode() == 0? "<" + ev.getKeyCode() + ">":
					KeyEvent.getKeyText(ev.getKeyCode());
	}

	public static void main(String[] args) {
		new KBDCheck().start();
	}

}
