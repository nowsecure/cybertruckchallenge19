// Eduardo Novella < enovella@nowsecure.com >
// CyberTruck Challenge 2019 -- Android Security Workshop

const arch = Process.arch;

Java.perform(function() {

  // TamperProof bypass
  Java.use("org.nowsecure.cybertruck.detections.HookDetector").isFridaServerInDevice.implementation = function () {
      // Before original call
      console.log("[TamperProof] - bypassed")

      // Original call
      // We bypass the anti-Frida just by skipping the original call


      // After the call
      return false;
  };

  // Challenge1 - staticKey
  const desKeySpec = Java.use('javax.crypto.spec.DESKeySpec').$init.overload('[B');
  desKeySpec.implementation = function (keyBytes) {

      // Before original call
      const deskey = convertToString(keyBytes);
      console.log("[CH1] sK: " + deskey)

      // Original call
      const ret = desKeySpec.call(this, keyBytes);

      // After the call

      return ret;
  };

  // Challenge1 - dynamicKey
  const generateDynamicKey = Java.use("org.nowsecure.cybertruck.keygenerators.Challenge1").generateDynamicKey;
  generateDynamicKey.implementation = function (bArr) {
      // Before original call
      // Do your stuff here

      // Original call
      var ret = generateDynamicKey.call(this, bArr);

      // After the call
      const a = convertToString(bArr);
      const dynKey = ba2hex(ret);
      console.log("[CH1] in: " + a);
      console.log("[CH1] dK: " + dynKey);
      return ret;
  };


  // Challenge2 - static & dynamic
  var aes = Java.use("org.nowsecure.cybertruck.keygenerators.a").a.overload('[B','[B');
  aes.implementation = function (bArr, b1) {
      // Before original call
      var data = convertToString(bArr);
      var aes_key = convertToString(b1);
      console.log("[CH2] in: " + data);
      console.log("[CH2] sK: " + aes_key);

      // Original call
      var ret = aes.call(this, bArr, b1);

      // After the call
      const dynKey = ba2hex(ret);
      console.log("[CH2] dK: " + dynKey);

      return ret;
  };

});


function ba2hex(bufArray) {
    var uint8arr = new Uint8Array(bufArray);
    if (!uint8arr) {
        return '';
    }

    var hexStr = '';
    for (var i = 0; i < uint8arr.length; i++) {
        var hex = (uint8arr[i] & 0xff).toString(16);
        hex = (hex.length === 1) ? '0' + hex : hex;
        hexStr += hex;
    }

    return hexStr.toLowerCase();
}

function convertToString(a) {
   var o = "";
   for (var i = 0; i < a.length; i++) {
       o += String.fromCharCode(mod(a[i], 256));
   }
   return o;
}

function mod(a, b) {
   return ((a % b) + b) % b;
}


function hd (p,length) {
  console.log(hexdump(ptr(p), {offset:0,length:length,header:false,ansi:true}));
}

Java.perform(function() {
    const System = Java.use('java.lang.System');
    const Runtime = Java.use('java.lang.Runtime');
    const SystemLoad = System.loadLibrary.overload('java.lang.String');
    const VMStack = Java.use('dalvik.system.VMStack');

    SystemLoad.implementation = function(library) {
        console.log("[Java] Loading dynamic library => " + library);
        try {
            const loaded = Runtime.getRuntime().loadLibrary0(VMStack.getCallingClassLoader(), library);

            if (library.includes("native-lib")){
                hookExports("libnative-lib.so");
            }
            return loaded;
        } catch(ex) {
            console.log(ex);
        }
    };
});

function hookExports(libtarget) {
  Process.enumerateModules({
    onMatch: function (module) {
      var base = module.base;
      var lib = module.name;

      if (module.name === libtarget) {
        if (!base) {
          console.log( lib + " isnt loaded yet. Returning now...");
          return 0;
        } else {
          console.log("[*] " + lib + " baseaddress: " + base);
        }

        // Exports
        Module.enumerateExports(module.name, {
          onMatch: function (exports) {
            console.log("exports->lib:" + module.name + " name:" + exports.name + " addr:" + exports.address + " offset:" + ptr(exports.address.sub(base)) + " type:" + exports.type);

            if (exports.name === "Java_org_nowsecure_cybertruck_MainActivity_init") {

              if (arch === 'ia32') {

                // Print binary strings
                console.log("[ro.data strings]");
                const p = base.add(0x074d);
                hd(p,64);

                //  Android x86 emulator
                //  000106ca 0f be 4c 0d b0   MOVSX     ECX,byte ptr [EBP + ECX*0x1 + -0x50]
                //  000106cf 31 c8            XOR       len,ECX
                //  000106d1 88 c2            MOV       DL,len
                const exor = 0x06cf;
                Interceptor.attach(base.add(exor), function () {
                  //console.log(JSON.stringify(this.context));
                  var x = this.context.eax;
                  var y = this.context.ecx;
                  var z = x ^ y;
                  console.log(`[CH3] [sK]: eax^ecx: (${String.fromCharCode(x)}) [dK]:${x}^${y} -> ${String.fromCharCode(z)}`);
                });

              } else if (arch === 'arm64') {

                  // Print binary strings
                  console.log("[ro.data strings]");
                  const p = base.add(0x085d);
                  hd(p,64);
                  //  ARM64 solution
                  //  001007cc 4a 01 0b 4a     eor       w10,w10,w11
                  //  001007d0 a9 83 57 f8     ldur      x9,[x29, #out]
                  //  001007d0 a9 83 57 f8     ldur      x9,[x29, #out]
                  //  001007d4 2a 69 28 38     strb      w10,[x9, x8, LSL ]
                  const exor = 0x7cc;
                  Interceptor.attach(base.add(exor), function () {
                    //console.log(JSON.stringify(this.context));
                    var x = this.context.x10;
                    var y = this.context.x11;
                    var z = x ^ y;
                    console.log(`[CH3] [sK]:x10^x11: (${String.fromCharCode(x)}) [dK]:${x}^${y} -> ${String.fromCharCode(z)}`);
                  });

              }
            }
          },
          onComplete: function () {
          }
        });
      }
    },
    onComplete: function () {
    }
  });
}



