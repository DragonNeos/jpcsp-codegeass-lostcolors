#!/bin/sh
java -Xmx1024m -Xss2m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=64m -Djava.library.path=lib/linux-x86 -jar dist/jpcspcg.jar -fpsV=17 -vf_f_n_s=1  $@