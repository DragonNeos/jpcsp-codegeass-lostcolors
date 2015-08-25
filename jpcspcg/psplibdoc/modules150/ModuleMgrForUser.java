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

public class ModuleMgrForUser implements HLEModule {
	@Override
	public String getName() { return "ModuleMgrForUser"; }
	
	@Override
	public void installModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.addFunction(sceKernelLoadModuleByIDFunction, 0xB7F46618);
			mm.addFunction(sceKernelLoadModuleFunction, 0x977DE386);
			mm.addFunction(sceKernelLoadModuleMsFunction, 0x710F61B5);
			mm.addFunction(sceKernelLoadModuleBufferUsbWlanFunction, 0xF9275D98);
			mm.addFunction(sceKernelStartModuleFunction, 0x50F0C1EC);
			mm.addFunction(sceKernelStopModuleFunction, 0xD1FF982A);
			mm.addFunction(sceKernelUnloadModuleFunction, 0x2E0911AA);
			mm.addFunction(sceKernelSelfStopUnloadModuleFunction, 0xD675EBB8);
			mm.addFunction(sceKernelStopUnloadSelfModuleFunction, 0xCC1D3699);
			mm.addFunction(sceKernelGetModuleIdListFunction, 0x644395E2);
			mm.addFunction(sceKernelQueryModuleInfoFunction, 0x748CBED9);
			mm.addFunction(sceKernelGetModuleIdFunction, 0xF0A26395);
			mm.addFunction(sceKernelGetModuleIdByAddressFunction, 0xD8B73127);
			
		}
	}
	
	@Override
	public void uninstallModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.removeFunction(sceKernelLoadModuleByIDFunction);
			mm.removeFunction(sceKernelLoadModuleFunction);
			mm.removeFunction(sceKernelLoadModuleMsFunction);
			mm.removeFunction(sceKernelLoadModuleBufferUsbWlanFunction);
			mm.removeFunction(sceKernelStartModuleFunction);
			mm.removeFunction(sceKernelStopModuleFunction);
			mm.removeFunction(sceKernelUnloadModuleFunction);
			mm.removeFunction(sceKernelSelfStopUnloadModuleFunction);
			mm.removeFunction(sceKernelStopUnloadSelfModuleFunction);
			mm.removeFunction(sceKernelGetModuleIdListFunction);
			mm.removeFunction(sceKernelQueryModuleInfoFunction);
			mm.removeFunction(sceKernelGetModuleIdFunction);
			mm.removeFunction(sceKernelGetModuleIdByAddressFunction);
			
		}
	}
	
	
	public void sceKernelLoadModuleByID(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadModuleByID [0xB7F46618]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadModule [0x977DE386]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadModuleMs(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadModuleMs [0x710F61B5]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelLoadModuleBufferUsbWlan(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelLoadModuleBufferUsbWlan [0xF9275D98]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelStartModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelStartModule [0x50F0C1EC]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelStopModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelStopModule [0xD1FF982A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelUnloadModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelUnloadModule [0x2E0911AA]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelSelfStopUnloadModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelSelfStopUnloadModule [0xD675EBB8]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelStopUnloadSelfModule(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelStopUnloadSelfModule [0xCC1D3699]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelGetModuleIdList(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelGetModuleIdList [0x644395E2]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelQueryModuleInfo(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelQueryModuleInfo [0x748CBED9]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelGetModuleId(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelGetModuleId [0xF0A26395]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceKernelGetModuleIdByAddress(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceKernelGetModuleIdByAddress [0xD8B73127]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public final HLEModuleFunction sceKernelLoadModuleByIDFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelLoadModuleByID") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadModuleByID(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelLoadModuleByID(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelLoadModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelLoadModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadModuleMsFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelLoadModuleMs") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadModuleMs(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelLoadModuleMs(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelLoadModuleBufferUsbWlanFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelLoadModuleBufferUsbWlan") {
		@Override
		public final void execute(Processor processor) {
			sceKernelLoadModuleBufferUsbWlan(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelLoadModuleBufferUsbWlan(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelStartModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelStartModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelStartModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelStartModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelStopModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelStopModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelStopModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelStopModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelUnloadModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelUnloadModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelUnloadModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelUnloadModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelSelfStopUnloadModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelSelfStopUnloadModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelSelfStopUnloadModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelSelfStopUnloadModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelStopUnloadSelfModuleFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelStopUnloadSelfModule") {
		@Override
		public final void execute(Processor processor) {
			sceKernelStopUnloadSelfModule(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelStopUnloadSelfModule(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelGetModuleIdListFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelGetModuleIdList") {
		@Override
		public final void execute(Processor processor) {
			sceKernelGetModuleIdList(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelGetModuleIdList(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelQueryModuleInfoFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelQueryModuleInfo") {
		@Override
		public final void execute(Processor processor) {
			sceKernelQueryModuleInfo(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelQueryModuleInfo(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelGetModuleIdFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelGetModuleId") {
		@Override
		public final void execute(Processor processor) {
			sceKernelGetModuleId(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelGetModuleId(processor);";
		}
	};
    
	public final HLEModuleFunction sceKernelGetModuleIdByAddressFunction = new HLEModuleFunction("ModuleMgrForUser", "sceKernelGetModuleIdByAddress") {
		@Override
		public final void execute(Processor processor) {
			sceKernelGetModuleIdByAddress(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.ModuleMgrForUserModule.sceKernelGetModuleIdByAddress(processor);";
		}
	};
    
};
