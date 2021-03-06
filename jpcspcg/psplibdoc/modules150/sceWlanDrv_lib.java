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

public class sceWlanDrv_lib implements HLEModule {
	@Override
	public String getName() { return "sceWlanDrv_lib"; }
	
	@Override
	public void installModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.addFunction(sceWlanDevAttachFunction, 0x482CAE9A);
			mm.addFunction(sceWlanDevDetachFunction, 0xC9A8CAB7);
			mm.addFunction(sceWlanDevGetMacAddrFunction, 0x19E51F54);
			mm.addFunction(sceWlanDevIsGameModeFunction, 0x5E7C8D94);
			mm.addFunction(sceWlanGPPrevEstablishActiveFunction, 0x5ED4049A);
			mm.addFunction(sceWlanGPSendFunction, 0xB4D7CB74);
			mm.addFunction(sceWlanGPRecvFunction, 0xA447103A);
			mm.addFunction(sceWlanGPRegisterCallbackFunction, 0x9658C9F7);
			mm.addFunction(sceWlanGPUnRegisterCallbackFunction, 0x4C7F62E0);
			mm.addFunction(sceWlanDrv_lib_81579D36Function, 0x81579D36);
			mm.addFunction(sceWlanDrv_lib_5BAA1FE5Function, 0x5BAA1FE5);
			mm.addFunction(sceWlanDrv_lib_4C14BACAFunction, 0x4C14BACA);
			mm.addFunction(sceWlanDrv_lib_2D0FAE4EFunction, 0x2D0FAE4E);
			mm.addFunction(sceWlanDrv_lib_56F467CAFunction, 0x56F467CA);
			mm.addFunction(sceWlanSetWakeUpFunction, 0xFE8A0B46);
			mm.addFunction(sceWlanDrv_lib_40B0AA4AFunction, 0x40B0AA4A);
			mm.addFunction(sceWlanDevSetGPIOFunction, 0x7FF54BD2);
			mm.addFunction(sceWlanDevGetStateGPIOFunction, 0x05FE320C);
			mm.addFunction(sceWlanDrv_lib_8D5F551BFunction, 0x8D5F551B);
			
		}
	}
	
	@Override
	public void uninstallModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.removeFunction(sceWlanDevAttachFunction);
			mm.removeFunction(sceWlanDevDetachFunction);
			mm.removeFunction(sceWlanDevGetMacAddrFunction);
			mm.removeFunction(sceWlanDevIsGameModeFunction);
			mm.removeFunction(sceWlanGPPrevEstablishActiveFunction);
			mm.removeFunction(sceWlanGPSendFunction);
			mm.removeFunction(sceWlanGPRecvFunction);
			mm.removeFunction(sceWlanGPRegisterCallbackFunction);
			mm.removeFunction(sceWlanGPUnRegisterCallbackFunction);
			mm.removeFunction(sceWlanDrv_lib_81579D36Function);
			mm.removeFunction(sceWlanDrv_lib_5BAA1FE5Function);
			mm.removeFunction(sceWlanDrv_lib_4C14BACAFunction);
			mm.removeFunction(sceWlanDrv_lib_2D0FAE4EFunction);
			mm.removeFunction(sceWlanDrv_lib_56F467CAFunction);
			mm.removeFunction(sceWlanSetWakeUpFunction);
			mm.removeFunction(sceWlanDrv_lib_40B0AA4AFunction);
			mm.removeFunction(sceWlanDevSetGPIOFunction);
			mm.removeFunction(sceWlanDevGetStateGPIOFunction);
			mm.removeFunction(sceWlanDrv_lib_8D5F551BFunction);
			
		}
	}
	
	
	public void sceWlanDevAttach(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevAttach [0x482CAE9A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDevDetach(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevDetach [0xC9A8CAB7]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDevGetMacAddr(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevGetMacAddr [0x19E51F54]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDevIsGameMode(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevIsGameMode [0x5E7C8D94]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanGPPrevEstablishActive(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanGPPrevEstablishActive [0x5ED4049A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanGPSend(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanGPSend [0xB4D7CB74]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanGPRecv(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanGPRecv [0xA447103A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanGPRegisterCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanGPRegisterCallback [0x9658C9F7]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanGPUnRegisterCallback(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanGPUnRegisterCallback [0x4C7F62E0]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_81579D36(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_81579D36 [0x81579D36]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_5BAA1FE5(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_5BAA1FE5 [0x5BAA1FE5]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_4C14BACA(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_4C14BACA [0x4C14BACA]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_2D0FAE4E(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_2D0FAE4E [0x2D0FAE4E]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_56F467CA(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_56F467CA [0x56F467CA]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanSetWakeUp(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanSetWakeUp [0xFE8A0B46]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_40B0AA4A(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_40B0AA4A [0x40B0AA4A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDevSetGPIO(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevSetGPIO [0x7FF54BD2]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDevGetStateGPIO(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDevGetStateGPIO [0x05FE320C]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceWlanDrv_lib_8D5F551B(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceWlanDrv_lib_8D5F551B [0x8D5F551B]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public final HLEModuleFunction sceWlanDevAttachFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevAttach") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevAttach(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevAttach(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDevDetachFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevDetach") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevDetach(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevDetach(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDevGetMacAddrFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevGetMacAddr") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevGetMacAddr(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevGetMacAddr(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDevIsGameModeFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevIsGameMode") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevIsGameMode(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevIsGameMode(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanGPPrevEstablishActiveFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanGPPrevEstablishActive") {
		@Override
		public final void execute(Processor processor) {
			sceWlanGPPrevEstablishActive(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanGPPrevEstablishActive(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanGPSendFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanGPSend") {
		@Override
		public final void execute(Processor processor) {
			sceWlanGPSend(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanGPSend(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanGPRecvFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanGPRecv") {
		@Override
		public final void execute(Processor processor) {
			sceWlanGPRecv(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanGPRecv(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanGPRegisterCallbackFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanGPRegisterCallback") {
		@Override
		public final void execute(Processor processor) {
			sceWlanGPRegisterCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanGPRegisterCallback(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanGPUnRegisterCallbackFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanGPUnRegisterCallback") {
		@Override
		public final void execute(Processor processor) {
			sceWlanGPUnRegisterCallback(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanGPUnRegisterCallback(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_81579D36Function = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_81579D36") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_81579D36(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_81579D36(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_5BAA1FE5Function = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_5BAA1FE5") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_5BAA1FE5(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_5BAA1FE5(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_4C14BACAFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_4C14BACA") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_4C14BACA(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_4C14BACA(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_2D0FAE4EFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_2D0FAE4E") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_2D0FAE4E(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_2D0FAE4E(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_56F467CAFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_56F467CA") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_56F467CA(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_56F467CA(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanSetWakeUpFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanSetWakeUp") {
		@Override
		public final void execute(Processor processor) {
			sceWlanSetWakeUp(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanSetWakeUp(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_40B0AA4AFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_40B0AA4A") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_40B0AA4A(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_40B0AA4A(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDevSetGPIOFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevSetGPIO") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevSetGPIO(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevSetGPIO(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDevGetStateGPIOFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDevGetStateGPIO") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDevGetStateGPIO(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDevGetStateGPIO(processor);";
		}
	};
    
	public final HLEModuleFunction sceWlanDrv_lib_8D5F551BFunction = new HLEModuleFunction("sceWlanDrv_lib", "sceWlanDrv_lib_8D5F551B") {
		@Override
		public final void execute(Processor processor) {
			sceWlanDrv_lib_8D5F551B(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceWlanDrv_libModule.sceWlanDrv_lib_8D5F551B(processor);";
		}
	};
    
};
