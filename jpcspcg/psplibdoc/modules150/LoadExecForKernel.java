/* This autogenerated file is part of jpcsp. */
/*
This file is part of jpcsp.

Jpcsp is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Jpcsp is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */

package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;

import jpcsp.Memory;
import jpcsp.Processor;

import jpcsp.Allegrex.CpuState; // New-Style Processor

public class LoadExecForKernel implements HLEModule {
	@Override
	public String getName() { return "LoadExecForKernel"; }
	
	@Override
	public void installModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.addFunction(sceKernelLoadExecFunction, 0xBD2F1094);
			mm.addFunction(sceKernelExitGameWithStatusFunction, 0x2AC9954B);
			mm.addFunction(sceKernelExitGameFunction, 0x05572A5F);
			mm.addFunction(sceKernelLoadExecVSHFromHostFunction, 0xAC085B9E);
			mm.addFunction(sceKernelLoadExecVSHDiscFunction, 0x1B97BDB3);
			mm.addFunction(sceKernelLoadExecVSHDiscUpdaterFunction, 0x821BE114);
			mm.addFunction(sceKernelLoadExecVSHDiscDebugFunction, 0x7B7C47EF);
			mm.addFunction(sceKernelLoadExecBufferVSHUsbWlanFunction, 0x015DA036);
			mm.addFunction(sceKernelLoadExecBufferVSHUsbWlanDebugFunction, 0x4F41E75E);
			mm.addFunction(sceKernelLoadExecVSHMs1Function, 0x31DF42BF);
			mm.addFunction(sceKernelLoadExecVSHMs2Function, 0x28D0D249);
			mm.addFunction(sceKernelLoadExecVSHMs3Function, 0x70901231);
			mm.addFunction(sceKernelExitVSHVSHFunction, 0xA3D5E142);
			mm.addFunction(sceKernelLoadExecBufferVSHPlainFunction, 0x62C459E1);
			mm.addFunction(sceKernelLoadExecBufferVSHFromHostFunction, 0x918782E8);
			mm.addFunction(sceKernelLoadExecBufferPlain0Function, 0xBB28E9B7);
			mm.addFunction(sceKernelLoadExecBufferPlainFunction, 0x71A1D738);
			mm.addFunction(sceKernelLoadExecFromHostFunction, 0x4D5C75BE);
			mm.addFunction(sceKernelRegisterExitCallbackFunction, 0x4AC57943);
			mm.addFunction(sceKernelUnregisterExitCallbackFunction, 0xD9739B89);
			mm.addFunction(sceKernelCheckExitCallbackFunction, 0x659188E1);
			mm.addFunction(sceKernelInvokeExitCallbackFunction, 0x62A27008);
			
		}
	}
	
	@Override
	public void uninstallModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.removeFunction(sceKernelLoadExecFunction);
			mm.removeFunction(sceKernelExitGameWithStatusFunction);
			mm.removeFunction(sceKernelExitGameFunction);
			mm.removeFunction(sceKernelLoadExecVSHFromHostFunction);
			mm.removeFunction(sceKernelLoadExecVSHDiscFunction);
			mm.removeFunction(sceKernelLoadExecVSHDiscUpdaterFunction);
			mm.removeFunction(sceKernelLoadExecVSHDiscDebugFunction);
			mm.removeFunction(sceKernelLoadExecBufferVSHUsbWlanFunction);
			mm.removeFunction(sceKernelLoadExecBufferVSHUsbWlanDebugFunction);
			mm.removeFunction(sceKernelLoadExecVSHMs1Function);
			mm.removeFunction(sceKernelLoadExecVSHMs2Function);
			mm.removeFunction(sceKernelLoadExecVSHMs3Function);
			mm.removeFunction(sceKernelExitVSHVSHFunction);
			mm.removeFunction(sceKernelLoadExecBufferVSHPlainFunction);
			mm.removeFunction(sceKernelLoadExecBufferVSHFromHostFunction);
			mm.removeFunction(sceKernelLoadExecBufferPlain0Function);
			mm.removeFunction(sceKernelLoadExecBufferPlainFunction);
			mm.removeFunction(sceKernelLoadExecFromHostFunction);
			mm.removeFunction(sceKernelRegisterExitCallbackFunction);
			mm.removeFunction(sceKernelUnregisterExitCallbackFunction);
			mm.removeFunction(sceKernelCheckExitCallbackFunction);
			mm.removeFunction(sceKernelInvokeExitCallbackFunction);
			
		}
	}
	
	
	public void sceKernelLoadExec(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExec [0xBD2F1094]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelExitGameWithStatus(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelExitGameWithStatus [0x2AC9954B]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelExitGame(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelExitGame [0x05572A5F]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHFromHost(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHFromHost [0xAC085B9E]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHDisc(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHDisc [0x1B97BDB3]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHDiscUpdater(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHDiscUpdater [0x821BE114]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHDiscDebug(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHDiscDebug [0x7B7C47EF]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferVSHUsbWlan(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferVSHUsbWlan [0x015DA036]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferVSHUsbWlanDebug(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferVSHUsbWlanDebug [0x4F41E75E]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHMs1(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHMs1 [0x31DF42BF]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHMs2(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHMs2 [0x28D0D249]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecVSHMs3(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecVSHMs3 [0x70901231]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelExitVSHVSH(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelExitVSHVSH [0xA3D5E142]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferVSHPlain(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferVSHPlain [0x62C459E1]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferVSHFromHost(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferVSHFromHost [0x918782E8]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferPlain0(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferPlain0 [0xBB28E9B7]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecBufferPlain(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecBufferPlain [0x71A1D738]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadExecFromHost(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadExecFromHost [0x4D5C75BE]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelRegisterExitCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelRegisterExitCallback [0x4AC57943]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelUnregisterExitCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelUnregisterExitCallback [0xD9739B89]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelCheckExitCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelCheckExitCallback [0x659188E1]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelInvokeExitCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelInvokeExitCallback [0x62A27008]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public final HLEModuleFunction sceKernelLoadExecFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExec") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExec(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExec(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelExitGameWithStatusFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelExitGameWithStatus") {
		@Override
		public final void execute(Processor processor) {
			sceKernelExitGameWithStatus(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelExitGameWithStatus(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelExitGameFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelExitGame") {
		@Override
		public final void execute(Processor processor) {
			sceKernelExitGame(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelExitGame(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHFromHostFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHFromHost") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHFromHost(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHFromHost(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHDiscFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHDisc") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHDisc(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHDisc(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHDiscUpdaterFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHDiscUpdater") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHDiscUpdater(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHDiscUpdater(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHDiscDebugFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHDiscDebug") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHDiscDebug(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHDiscDebug(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferVSHUsbWlanFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferVSHUsbWlan") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferVSHUsbWlan(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferVSHUsbWlan(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferVSHUsbWlanDebugFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferVSHUsbWlanDebug") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferVSHUsbWlanDebug(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferVSHUsbWlanDebug(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHMs1Function = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHMs1") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHMs1(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHMs1(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHMs2Function = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHMs2") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHMs2(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHMs2(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecVSHMs3Function = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecVSHMs3") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecVSHMs3(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecVSHMs3(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelExitVSHVSHFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelExitVSHVSH") {
		@Override
		public final void execute(Processor processor) {
			sceKernelExitVSHVSH(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelExitVSHVSH(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferVSHPlainFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferVSHPlain") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferVSHPlain(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferVSHPlain(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferVSHFromHostFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferVSHFromHost") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferVSHFromHost(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferVSHFromHost(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferPlain0Function = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferPlain0") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferPlain0(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferPlain0(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecBufferPlainFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecBufferPlain") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecBufferPlain(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecBufferPlain(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadExecFromHostFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelLoadExecFromHost") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadExecFromHost(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelLoadExecFromHost(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelRegisterExitCallbackFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelRegisterExitCallback") {
		@Override
		public final void execute(Processor processor) {
			sceKernelRegisterExitCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelRegisterExitCallback(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelUnregisterExitCallbackFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelUnregisterExitCallback") {
		@Override
		public final void execute(Processor processor) {
			sceKernelUnregisterExitCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelUnregisterExitCallback(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelCheckExitCallbackFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelCheckExitCallback") {
		@Override
		public final void execute(Processor processor) {
			sceKernelCheckExitCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelCheckExitCallback(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelInvokeExitCallbackFunction = new HLEModuleFunction("LoadExecForKernel", "sceKernelInvokeExitCallback") {
		@Override
		public final void execute(Processor processor) {
			sceKernelInvokeExitCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.LoadExecForKernelModule.sceKernelInvokeExitCallback(processor);";
		}
	};
    
};
