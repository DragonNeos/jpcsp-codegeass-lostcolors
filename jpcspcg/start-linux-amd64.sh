#!/bin/sh
java -Xmx512m -Xss2m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=64m -Djava.library.path=lib/linux-amd64 -jar dist/jpcspcg.jar -fpsV=17 -vf_f_n_s=1  $@
