// Eduardo Novella < enovella@nowsecure.com >
// CyberTruck Challenge 2019 -- Android Security Workshop

Java.perform(function() {

  // Challenge1 - staticKey
  const desKeySpec = Java.use('javax.crypto.spec.DESKeySpec').$init.overload('[B');
  desKeySpec.implementation = function (keyBytes) {

      // Before original call
      console.log("Hey there! Running code before the Class Challenge1 gets called!");
      // TODO

      // Original call
      const ret = desKeySpec.call(this, keyBytes);

      // After the call
      console.log("Hey there! Running code after the Class Challenge1 got called!");
      // TODO

      return ret;
  };

  // Challenge1 - dynamicKey
  const generateDynamicKey = Java.use("org.nowsecure.cybertruck.keygenerators.Challenge1").generateDynamicKey;
  generateDynamicKey.implementation = function (bArr) {

      // Before original call
      console.log("Hey there! I am Challenge1, hook the dynamic key!")

      // Original call
      // TODO

      // After the call
      // TODO
  };

  // Challenge2 - static & dynamic


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
            console.log("exports->lib:" + module.name + " name:" + exports.name +
              " addr:" + exports.address + " offset:" + ptr(exports.address.sub(base)) + " type:" + exports.type);

            if (exports.name === "Java_org_nowsecure_cybertruck_MainActivity_init") {

              const offset = 0x000000; // Choose where to inspect memory via static analysis
              Interceptor.attach(base.add(offset), function () {
                console.log(JSON.stringify(this.context));
                // Do stuff
              });

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



