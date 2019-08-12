# Mobile CTF Description
A new mobile remote keyless system "CyberTruck" has been implemented by one of the most well-known car security companies "NowSecure Mobile Vehicles". The car security company has ensured that the system is entirely uncrackable and therefore attackers will not be able to recover secrets within the mobile application.

If you are an experienced Android reverser, then enable the "tamperproof" button to harden the application before unlocking your cars. Your goal will consist on recovering up to 6 secrets in the application.

# Flags
```c
>> Challenge1 to unlock car1
Title: "DES key: Completely Keyless. Completely safe."

There is a secret used to create a DES key. Can you tell me which one?
	50  - static  = s3cr3t$_n3veR_mUst_bE_h4rdc0d3d_m4t3!

There is a token generated at runtime to unlock the carid=1. Can you get it? (flag must be summitted in hexa all lowercase)
	100 - dynamic = 046e04ff67535d25dfea022033fcaaf23606b95a5c07a8c6


>> Challenge2 to unlock car2
Title: "AES key: Your Cell Mobile Is Your Key"

This challenge has been obfuscated with ProGuard, therefore you will not recover the AES key.
	50 - static   = d474_47_r357_mu57_pR073C73D700!!

There is a token generated at runtime to unlock the carid=2. Can you get it? (flag must be summitted in hexa all lowercase)
	100 - dynamic = 512100f7cc50c76906d23181aff63f0d642b3d947f75d360b6b15447540e4f16


>> Challenge3 to unlock car3
Title: "Mr Truck: Unlock me Baby!"

There is an interesting string in the native code. Can you catch it?
	50 -  static  = Native_c0d3_1s_h4rd3r_To_r3vers3

Get the secret generated at runtime to unlock the carid=3. Security by obscurity is not a great design. Use real crypto! (hint: check the length when summitting the secret!)
	200 - dynamic = backd00r$Mu$tAlw4ysBeF0rb1dd3n$$
```

# Hands-on with R2frida
First of all, we start Frida server in our Android x86 emulator or physical mobile device:
```c
[edu@NowSecure solutions] > adb shell
jasmine_sprout:/ $ su
jasmine_sprout:/ # cd  /data/local/tmp
jasmine_sprout:/data/local/tmp # ./frida-server -D
```

Secondly, we can inject code in the target by spawing the process via r2frida and extract all the static and dynamic flags:
```c
[edu@NowSecure solutions] > r2 frida://spawn/usb//org.nowsecure.cybertruck
 -- Have you seen the latest radare2 TV spot?
[0x00000000]> \. ./solutions.js
[0x00000000]> \dc
resumed spawned process.
[0x00000000]> [Java] Loading dynamic library => qti_performance
[Java] Loading dynamic library => native-lib
[*] libnative-lib.so baseaddress: 0x75cc717000
exports->lib:libnative-lib.so name:Java_org_nowsecure_cybertruck_MainActivity_init addr:0x75cc7176e8 offset:0x6e8 type:function
[ro.data strings]
75cc71785d  4e 61 74 69 76 65 5f 63 30 64 33 5f 31 73 5f 68  Native_c0d3_1s_h
75cc71786d  34 72 64 33 72 5f 54 6f 5f 72 33 76 65 72 73 33  4rd3r_To_r3vers3
75cc71787d  2c 00 17 02 12 55 6f 11 14 29 46 7b 45 32 33 1f  ,....Uo..)F{E23.
75cc71788d  00 0b 17 71 17 19 64 1d 3d 43 57 12 56 1c 57 17  ...q..d.=CW.V.W.
[TamperProof] - bypassed
[CH1] sK: s3cr3t$_n3veR_mUst_bE_h4rdc0d3d_m4t3!
[CH1] in: CyB3r_tRucK_Ch4113ng3
[CH1] dK: 046e04ff67535d25dfea022033fcaaf23606b95a5c07a8c6
[CH2] in: uncr4ck4ble_k3yle$$
[CH2] sK: d474_47_r357_mu57_pR073C73D700!!
[CH2] dK: 512100f7cc50c76906d23181aff63f0d642b3d947f75d360b6b15447540e4f16
[CH3] [sK]:x10^x11: (N) [dK]:0x4e^0x2c -> b
[CH3] [sK]:x10^x11: (a) [dK]:0x61^0x0 -> a
[CH3] [sK]:x10^x11: (t) [dK]:0x74^0x17 -> c
[CH3] [sK]:x10^x11: (i) [dK]:0x69^0x2 -> k
[CH3] [sK]:x10^x11: (v) [dK]:0x76^0x12 -> d
[CH3] [sK]:x10^x11: (e) [dK]:0x65^0x55 -> 0
[CH3] [sK]:x10^x11: (_) [dK]:0x5f^0x6f -> 0
[CH3] [sK]:x10^x11: (c) [dK]:0x63^0x11 -> r
[CH3] [sK]:x10^x11: (0) [dK]:0x30^0x14 -> $
[CH3] [sK]:x10^x11: (d) [dK]:0x64^0x29 -> M
[CH3] [sK]:x10^x11: (3) [dK]:0x33^0x46 -> u
[CH3] [sK]:x10^x11: (_) [dK]:0x5f^0x7b -> $
[CH3] [sK]:x10^x11: (1) [dK]:0x31^0x45 -> t
[CH3] [sK]:x10^x11: (s) [dK]:0x73^0x32 -> A
[CH3] [sK]:x10^x11: (_) [dK]:0x5f^0x33 -> l
[CH3] [sK]:x10^x11: (h) [dK]:0x68^0x1f -> w
[CH3] [sK]:x10^x11: (4) [dK]:0x34^0x0 -> 4
[CH3] [sK]:x10^x11: (r) [dK]:0x72^0xb -> y
[CH3] [sK]:x10^x11: (d) [dK]:0x64^0x17 -> s
[CH3] [sK]:x10^x11: (3) [dK]:0x33^0x71 -> B
[CH3] [sK]:x10^x11: (r) [dK]:0x72^0x17 -> e
[CH3] [sK]:x10^x11: (_) [dK]:0x5f^0x19 -> F
[CH3] [sK]:x10^x11: (T) [dK]:0x54^0x64 -> 0
[CH3] [sK]:x10^x11: (o) [dK]:0x6f^0x1d -> r
[CH3] [sK]:x10^x11: (_) [dK]:0x5f^0x3d -> b
[CH3] [sK]:x10^x11: (r) [dK]:0x72^0x43 -> 1
[CH3] [sK]:x10^x11: (3) [dK]:0x33^0x57 -> d
[CH3] [sK]:x10^x11: (v) [dK]:0x76^0x12 -> d
[CH3] [sK]:x10^x11: (e) [dK]:0x65^0x56 -> 3
[CH3] [sK]:x10^x11: (r) [dK]:0x72^0x1c -> n
[CH3] [sK]:x10^x11: (s) [dK]:0x73^0x57 -> $
[CH3] [sK]:x10^x11: (3) [dK]:0x33^0x17 -> $
```

That's all folks!
