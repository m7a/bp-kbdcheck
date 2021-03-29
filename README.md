---
section: 32
x-masysma-name: kbdcheck
title: Ma_Sys.ma KDB Check
date: 2021/03/28 22:36:01
lang: en-US
author: ["Linux-Fan, Ma_Sys.ma (Ma_Sys.ma@web.de)"]
keywords: ["kbd", "keyboard", "hardware", "java", "check", "chatter"]
x-masysma-version: 1.0.0
x-masysma-repository: https://www.github.com/m7a/bp-kbdcheck
x-masysma-website: https://masysma.lima-city.de/32/kbdcheck.xhtml
x-masysma-owned: 1
x-masysma-copyright: |
  Copyright (c) 2019, 2021 Ma_Sys.ma.
  For further info send an e-mail to Ma_Sys.ma@web.de.
---
Name
====

`kbdcheck` -- measure relative keyboard press delays

Synopsis
========

	java -jar kbd_check.jar

Description
===========

The purpose of this program is to debug occurrences of key chatter and other
key-press related keyboard issues. It opens a window where key presses can be
entered. On the console, it logs two different event types (DOWNUP/PRESS) and
different times.

All times are displayed in `ms` and collected as reported to the Java GUI
events. This program does _not_ interface with the hardware directly!

### DOWNUP-Times

 1. Time from the previous event (down/up) to the current _up_ event.
    This is the time it took to release a key.
 2. Time from the previous event (down/up) to the current _down_ event.
    This represents the minimum time between key presses.
 3. Time between the current key's down and up events

If you are typing slowly, times (1) and (3) will match and (2) will probably
be the largest. If you are typing quickly, different times may be observed
depending on how exactly you are typing. For me, it is common that (3) is the
largest, (2) the smallest and (1) something in between.

### PRESS-Times

This reports the time between the current and the previous key press.
Additionally, a line with the current input buffer is printed. For most
purposes, it is better to rely on the DOWNUP-times.

Examples
========

## Slow Typing (Razer Huntsman)

~~~
PRESS;          t;     1951.9657;;;  t_______________________________________
DOWNUP;         T;      110.2587;     1949.9546;      110.2587
PRESS;          e;      232.3907;;;  te______________________________________
DOWNUP;         E;       86.9473;      123.8840;       86.9473
PRESS;          s;      287.9468;;;  tes_____________________________________
DOWNUP;         S;      124.0631;      200.9908;      124.0631
PRESS;          t;      278.9432;;;  test____________________________________
DOWNUP;         T;      135.9751;      154.9109;      135.9751
PRESS;           ;      894.2690;;;  ________________________________________
DOWNUP;     Space;      145.6667;      758.2782;      145.6667
PRESS;          p;      578.0508;;;  p_______________________________________
DOWNUP;         P;      105.7368;      432.3419;      105.7368
PRESS;          u;      356.5125;;;  pu______________________________________
DOWNUP;         U;      103.0786;      250.8598;      103.0786
PRESS;          b;      299.6446;;;  pub_____________________________________
DOWNUP;         B;      137.3324;      196.5013;      137.3324
PRESS;          l;      278.2506;;;  publ____________________________________
DOWNUP;         L;      128.9334;      140.9877;      128.9334
PRESS;          i;      395.2522;;;  publi___________________________________
DOWNUP;         I;       93.0813;      266.2799;       93.0813
PRESS;          c;      316.2890;;;  public__________________________________
DOWNUP;         C;       95.9327;      223.1921;       95.9327
~~~

Observe that

 * All characters are first released before the next one is pressed.
   Hence times (1) and (3) match.
 * there are large times between the individual key presses which (well above
   100ms).
 * the PRESS message precedes the DOWNUP message for the same key.

## Fast Typing (Razer Huntsman)

~~~
PRESS;          v;      106.0943;;;  v_______________________________________
DOWNUP;     Space;       12.9478;       55.0448;      118.9655
PRESS;          o;       54.8649;;;  vo______________________________________
DOWNUP;         V;       80.0867;       65.1949;      135.0107
PRESS;          i;       90.0224;;;  voi_____________________________________
DOWNUP;         O;       13.9205;       41.9762;      103.9425
PRESS;          d;      122.0572;;;  void____________________________________
DOWNUP;         I;        8.8649;        9.9353;      130.9077
PRESS;           ;       57.9841;;;  ________________________________________
DOWNUP;         D;       45.9227;      108.1223;      103.8971
PRESS;          m;       72.7116;;;  m_______________________________________
DOWNUP;     Space;       52.8453;       49.1094;      125.6446
PRESS;          a;       91.0303;;;  ma______________________________________
DOWNUP;         M;       38.8824;       26.8766;      129.9390
PRESS;          i;       87.2527;;;  mai_____________________________________
PRESS;          n;       40.0554;;;  main____________________________________
DOWNUP;         A;        1.3444;       38.2114;      128.5576
DOWNUP;         I;       70.5181;       48.2696;      111.9237
DOWNUP;         N;       58.0162;       40.0613;      129.8786
PRESS;           ;      130.3094;;;  ________________________________________
DOWNUP;     Space;      107.5117;        0.3988;      107.5117
PRESS;          S;      280.6463;;;  S_______________________________________
DOWNUP;     Shift;       75.8604;      121.5900;      127.4500
PRESS;          t;      119.0456;;;  St______________________________________
DOWNUP;         S;        2.9868;       51.5896;      122.0115
DOWNUP;         T;       60.7071;       43.1644;       63.6939
PRESS;          r;      124.9267;;;  Str_____________________________________
PRESS;          i;       16.9273;;;  Stri____________________________________
PRESS;          n;       63.2370;;;  Strin___________________________________
DOWNUP;         R;       37.6096;       61.2308;      117.7458
DOWNUP;         I;       29.0342;       16.9862;      129.7938
DOWNUP;         N;       39.1207;       63.1500;      105.7645
~~~

Observe that

 * The next DOWNUP is already registered before the respective PRESS event is
   generated.
 * Times between individual presses are sometimes very short
 * Time between press/release (3) is longer than time (1)

One can conclude that sometimes, before the one key is registered as pressed,
the next one has already been hit. Also, these times confirm why I have a
tendency to get the order of characters mixed up while typing :)

## Fast Typing (Microsoft Wired 200)

~~~
PRESS;          b;       95.9626;;;  pub_____________________________________
DOWNUP;         B;       80.0866;       32.0105;       80.0866
PRESS;          l;       80.2985;;;  publ____________________________________
DOWNUP;         L;       31.4816;        0.3142;       31.4816
PRESS;          i;       95.7593;;;  publi___________________________________
DOWNUP;         I;       80.1575;       64.1112;       80.1575
PRESS;          c;       80.0967;;;  public__________________________________
DOWNUP;         C;       63.5751;        0.1780;       63.5751
PRESS;           ;       79.6074;;;  ________________________________________
PRESS;          s;       80.1408;;;  s_______________________________________
DOWNUP;     Space;       15.9091;       15.9443;       95.9576
PRESS;          t;       64.0751;;;  st______________________________________
DOWNUP;         S;       16.0244;       80.0485;       80.1141
DOWNUP;         T;       64.0320;       48.1806;       80.0563
PRESS;          a;       95.9686;;;  sta_____________________________________
PRESS;          t;       79.9846;;;  stat____________________________________
DOWNUP;         A;       64.0084;       15.9043;      144.0669
PRESS;          i;       64.3238;;;  stati___________________________________
DOWNUP;         T;       15.6293;       80.0585;       79.9842
DOWNUP;         I;       47.8370;        0.3465;       63.4663
PRESS;          c;      127.6606;;;  static__________________________________
PRESS;           ;       48.0395;;;  ________________________________________
DOWNUP;         C;       32.0589;       64.1537;       80.0393
DOWNUP;     Space;       48.0834;       47.9804;       80.1423
PRESS;          v;      128.0320;;;  v_______________________________________
PRESS;          o;       32.0890;;;  vo______________________________________
DOWNUP;         V;       63.9258;       47.9003;       96.0628
DOWNUP;         O;        0.3272;       32.1370;       64.2530
PRESS;          i;       96.0925;;;  voi_____________________________________
DOWNUP;         I;       79.7778;       31.8350;       79.7778
PRESS;          d;       95.6524;;;  void____________________________________
DOWNUP;         D;       79.9270;       15.8886;       79.9270
PRESS;           ;       79.9461;;;  ________________________________________
DOWNUP;     Space;       96.0957;        0.1314;       96.0957
PRESS;          m;       96.5113;;;  m_______________________________________
DOWNUP;         M;       79.6891;        0.3030;       79.6891
PRESS;          a;       79.8488;;;  ma______________________________________
PRESS;          i;       95.6782;;;  mai_____________________________________
DOWNUP;         A;       16.0069;        0.2172;      111.6688
DOWNUP;         I;       79.8823;       95.6619;       95.8892
PRESS;          n;       96.0282;;;  main____________________________________
PRESS;           ;       64.1541;;;  ________________________________________
DOWNUP;         N;       15.8204;        0.1830;       79.7917
~~~

Note that compared to the previous output, events are more often in sequence
than before. Additionally, the individual key press duration time (3) is
around 40 ms smaller for most entries.

## Key Chatter (UNICOMP Model M?)

Here is an excerpt from another keyboard which shows key chatter

~~~
DOWNUP;         A;      133.7903;      345.4194;      133.7903
PRESS;          7;      511.9192;;;  a77a77a77a7a7a7a7a7a7a7a7_______________
DOWNUP;         7;       57.9856;      378.1451;       57.9856
PRESS;          7;       69.7271;;;  a77a77a77a7a7a7a7a7a7a7a77______________
DOWNUP;         7;       18.1876;       11.7577;       18.1876
PRESS;          a;      544.6178;;;  a77a77a77a7a7a7a7a7a7a7a77a_____________
DOWNUP;         A;      131.9566;      526.3469;      131.9566
PRESS;          7;      477.9078;;;  a77a77a77a7a7a7a7a7a7a7a77a7____________
DOWNUP;         7;       89.6220;      345.9802;       89.6220
PRESS;          a;      597.6554;;;  a77a77a77a7a7a7a7a7a7a7a77a7a___________
DOWNUP;         A;      120.0221;      508.0771;      120.0221
PRESS;          7;      510.1118;;;  a77a77a77a7a7a7a7a7a7a7a77a7a7__________
DOWNUP;         7;       59.9544;      390.0526;       59.9544
PRESS;          7;       72.0121;;;  a77a77a77a7a7a7a7a7a7a7a77a7a77_________
DOWNUP;         7;       15.7135;       12.0468;       15.7135
PRESS;          a;     1900.8568;;;  a77a77a77a7a7a7a7a7a7a7a77a7a77a________
DOWNUP;         A;      115.5355;     1884.9573;      115.5355
~~~

Here, key `7` appears double with astonishingly high probability. One can
distinguish the key chatter events from their short keypress duration of between
15.71 and 18.19 ms whereas the regular keypresses are at least 57.99 ms long.

Note further that here, times (1) and (3) match, indicating that keys were
hit in order.

Compilation
===========

If you have a Java compiler, you can use

	$ javac KBDCheck.java
	$ java KBDCheck

Alternatively, if you have `ant` and a Java Compiler, use

	$ ant jar

to build a runnable jarfile `kdb_check.jar` and

	$ ant clean

to cleanup the workspace.

See Also
========

 * [pressed_keys(32)](pressed_keys.xhtml)
 * [linux_x11_keyboard_configuration(37)](../37/linux_x11_keyboard_configuration.xhtml)
