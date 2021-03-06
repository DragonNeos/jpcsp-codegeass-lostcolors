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

public class sceRtc_driver implements HLEModule {
	@Override
	public String getName() { return "sceRtc_driver"; }
	
	@Override
	public void installModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.addFunction(sceRtcInitFunction, 0x912BEE56);
			mm.addFunction(sceRtcEndFunction, 0xCE27DE2F);
			mm.addFunction(sceRtcSuspendFunction, 0x9CC2797E);
			mm.addFunction(sceRtcResumeFunction, 0x48D07D70);
			mm.addFunction(sceRtcSynchronizeFunction, 0xF0B5571C);
			mm.addFunction(sceRtcSetConfFunction, 0x759937C5);
			mm.addFunction(sceRtcGetTickResolutionFunction, 0xC41C2853);
			mm.addFunction(sceRtcSetCurrentTickFunction, 0x9763C138);
			mm.addFunction(sceRtc_driver_17C26C00Function, 0x17C26C00);
			mm.addFunction(sceRtcGetCurrentTickFunction, 0x3F7AD767);
			mm.addFunction(sceRtc_driver_B44BDAEDFunction, 0xB44BDAED);
			mm.addFunction(sceRtcGetAccumulativeTimeFunction, 0x011F03C1);
			mm.addFunction(sceRtc_driver_029CA3B3Function, 0x029CA3B3);
			mm.addFunction(sceRtcGetCurrentClockFunction, 0x4CFA57B0);
			mm.addFunction(sceRtcGetCurrentClockLocalTimeFunction, 0xE7C27D1B);
			mm.addFunction(sceRtcConvertUtcToLocalTimeFunction, 0x34885E0D);
			mm.addFunction(sceRtcConvertLocalTimeToUTCFunction, 0x779242A2);
			mm.addFunction(sceRtcSetAlarmTickFunction, 0x7D1FBED3);
			mm.addFunction(sceRtcGetAlarmTickFunction, 0xC2DDBEB5);
			mm.addFunction(sceRtcIsLeapYearFunction, 0x42307A17);
			mm.addFunction(sceRtcGetDaysInMonthFunction, 0x05EF322C);
			mm.addFunction(sceRtcGetDayOfWeekFunction, 0x57726BC1);
			mm.addFunction(sceRtcCheckValidFunction, 0x4B1B5E82);
			mm.addFunction(sceRtcSetTime_tFunction, 0x3A807CC8);
			mm.addFunction(sceRtcGetTime_tFunction, 0x27C4594C);
			mm.addFunction(sceRtcSetDosTimeFunction, 0xF006F264);
			mm.addFunction(sceRtcGetDosTimeFunction, 0x36075567);
			mm.addFunction(sceRtcSetWin32FileTimeFunction, 0x7ACE4C04);
			mm.addFunction(sceRtcGetWin32FileTimeFunction, 0xCF561893);
			mm.addFunction(sceRtcSetTickFunction, 0x7ED29E40);
			mm.addFunction(sceRtcGetTickFunction, 0x6FF40ACC);
			mm.addFunction(sceRtcCompareTickFunction, 0x9ED0AE87);
			mm.addFunction(sceRtcTickAddTicksFunction, 0x44F45E05);
			mm.addFunction(sceRtcTickAddMicrosecondsFunction, 0x26D25A5D);
			mm.addFunction(sceRtcTickAddSecondsFunction, 0xF2A4AFE5);
			mm.addFunction(sceRtcTickAddMinutesFunction, 0xE6605BCA);
			mm.addFunction(sceRtcTickAddHoursFunction, 0x26D7A24A);
			mm.addFunction(sceRtcTickAddDaysFunction, 0xE51B4B7A);
			mm.addFunction(sceRtcTickAddWeeksFunction, 0xCF3A2CA8);
			mm.addFunction(sceRtcTickAddMonthsFunction, 0xDBF74F1B);
			mm.addFunction(sceRtcTickAddYearsFunction, 0x42842C77);
			mm.addFunction(sceRtcFormatRFC2822Function, 0xC663B3B9);
			mm.addFunction(sceRtcFormatRFC2822LocalTimeFunction, 0x7DE6711B);
			mm.addFunction(sceRtcFormatRFC3339Function, 0x0498FB3C);
			mm.addFunction(sceRtcFormatRFC3339LocalTimeFunction, 0x27F98543);
			mm.addFunction(sceRtcParseDateTimeFunction, 0xDFBC5F16);
			mm.addFunction(sceRtcParseRFC3339Function, 0x28E1E988);
			
		}
	}
	
	@Override
	public void uninstallModule(HLEModuleManager mm, int version) {
		if (version >= 150) {
		
			mm.removeFunction(sceRtcInitFunction);
			mm.removeFunction(sceRtcEndFunction);
			mm.removeFunction(sceRtcSuspendFunction);
			mm.removeFunction(sceRtcResumeFunction);
			mm.removeFunction(sceRtcSynchronizeFunction);
			mm.removeFunction(sceRtcSetConfFunction);
			mm.removeFunction(sceRtcGetTickResolutionFunction);
			mm.removeFunction(sceRtcSetCurrentTickFunction);
			mm.removeFunction(sceRtc_driver_17C26C00Function);
			mm.removeFunction(sceRtcGetCurrentTickFunction);
			mm.removeFunction(sceRtc_driver_B44BDAEDFunction);
			mm.removeFunction(sceRtcGetAccumulativeTimeFunction);
			mm.removeFunction(sceRtc_driver_029CA3B3Function);
			mm.removeFunction(sceRtcGetCurrentClockFunction);
			mm.removeFunction(sceRtcGetCurrentClockLocalTimeFunction);
			mm.removeFunction(sceRtcConvertUtcToLocalTimeFunction);
			mm.removeFunction(sceRtcConvertLocalTimeToUTCFunction);
			mm.removeFunction(sceRtcSetAlarmTickFunction);
			mm.removeFunction(sceRtcGetAlarmTickFunction);
			mm.removeFunction(sceRtcIsLeapYearFunction);
			mm.removeFunction(sceRtcGetDaysInMonthFunction);
			mm.removeFunction(sceRtcGetDayOfWeekFunction);
			mm.removeFunction(sceRtcCheckValidFunction);
			mm.removeFunction(sceRtcSetTime_tFunction);
			mm.removeFunction(sceRtcGetTime_tFunction);
			mm.removeFunction(sceRtcSetDosTimeFunction);
			mm.removeFunction(sceRtcGetDosTimeFunction);
			mm.removeFunction(sceRtcSetWin32FileTimeFunction);
			mm.removeFunction(sceRtcGetWin32FileTimeFunction);
			mm.removeFunction(sceRtcSetTickFunction);
			mm.removeFunction(sceRtcGetTickFunction);
			mm.removeFunction(sceRtcCompareTickFunction);
			mm.removeFunction(sceRtcTickAddTicksFunction);
			mm.removeFunction(sceRtcTickAddMicrosecondsFunction);
			mm.removeFunction(sceRtcTickAddSecondsFunction);
			mm.removeFunction(sceRtcTickAddMinutesFunction);
			mm.removeFunction(sceRtcTickAddHoursFunction);
			mm.removeFunction(sceRtcTickAddDaysFunction);
			mm.removeFunction(sceRtcTickAddWeeksFunction);
			mm.removeFunction(sceRtcTickAddMonthsFunction);
			mm.removeFunction(sceRtcTickAddYearsFunction);
			mm.removeFunction(sceRtcFormatRFC2822Function);
			mm.removeFunction(sceRtcFormatRFC2822LocalTimeFunction);
			mm.removeFunction(sceRtcFormatRFC3339Function);
			mm.removeFunction(sceRtcFormatRFC3339LocalTimeFunction);
			mm.removeFunction(sceRtcParseDateTimeFunction);
			mm.removeFunction(sceRtcParseRFC3339Function);
			
		}
	}
	
	
	public void sceRtcInit(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcInit [0x912BEE56]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcEnd(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcEnd [0xCE27DE2F]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSuspend(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSuspend [0x9CC2797E]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcResume(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcResume [0x48D07D70]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSynchronize(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSynchronize [0xF0B5571C]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetConf(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetConf [0x759937C5]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetTickResolution(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetTickResolution [0xC41C2853]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetCurrentTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetCurrentTick [0x9763C138]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtc_driver_17C26C00(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtc_driver_17C26C00 [0x17C26C00]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetCurrentTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetCurrentTick [0x3F7AD767]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtc_driver_B44BDAED(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtc_driver_B44BDAED [0xB44BDAED]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetAccumulativeTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetAccumulativeTime [0x011F03C1]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtc_driver_029CA3B3(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtc_driver_029CA3B3 [0x029CA3B3]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetCurrentClock(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetCurrentClock [0x4CFA57B0]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetCurrentClockLocalTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetCurrentClockLocalTime [0xE7C27D1B]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcConvertUtcToLocalTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcConvertUtcToLocalTime [0x34885E0D]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcConvertLocalTimeToUTC(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcConvertLocalTimeToUTC [0x779242A2]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetAlarmTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetAlarmTick [0x7D1FBED3]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetAlarmTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetAlarmTick [0xC2DDBEB5]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcIsLeapYear(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcIsLeapYear [0x42307A17]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetDaysInMonth(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetDaysInMonth [0x05EF322C]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetDayOfWeek(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetDayOfWeek [0x57726BC1]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcCheckValid(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcCheckValid [0x4B1B5E82]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetTime_t(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetTime_t [0x3A807CC8]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetTime_t(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetTime_t [0x27C4594C]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetDosTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetDosTime [0xF006F264]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetDosTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetDosTime [0x36075567]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetWin32FileTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetWin32FileTime [0x7ACE4C04]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetWin32FileTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetWin32FileTime [0xCF561893]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcSetTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcSetTick [0x7ED29E40]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcGetTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcGetTick [0x6FF40ACC]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcCompareTick(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcCompareTick [0x9ED0AE87]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddTicks(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddTicks [0x44F45E05]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddMicroseconds(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddMicroseconds [0x26D25A5D]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddSeconds(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddSeconds [0xF2A4AFE5]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddMinutes(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddMinutes [0xE6605BCA]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddHours(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddHours [0x26D7A24A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddDays(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddDays [0xE51B4B7A]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddWeeks(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddWeeks [0xCF3A2CA8]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddMonths(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddMonths [0xDBF74F1B]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcTickAddYears(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcTickAddYears [0x42842C77]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcFormatRFC2822(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcFormatRFC2822 [0xC663B3B9]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcFormatRFC2822LocalTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcFormatRFC2822LocalTime [0x7DE6711B]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcFormatRFC3339(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcFormatRFC3339 [0x0498FB3C]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcFormatRFC3339LocalTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcFormatRFC3339LocalTime [0x27F98543]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcParseDateTime(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcParseDateTime [0xDFBC5F16]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public void sceRtcParseRFC3339(Processor processor) {
		CpuState cpu = processor.cpu;

		Modules.log.debug("Unimplemented NID function sceRtcParseRFC3339 [0x28E1E988]");

		cpu.gpr[2] = 0xDEADC0DE;
	}
    
	public final HLEModuleFunction sceRtcInitFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcInit") {
		@Override
		public final void execute(Processor processor) {
			sceRtcInit(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcInit(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcEndFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcEnd") {
		@Override
		public final void execute(Processor processor) {
			sceRtcEnd(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcEnd(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSuspendFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSuspend") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSuspend(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSuspend(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcResumeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcResume") {
		@Override
		public final void execute(Processor processor) {
			sceRtcResume(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcResume(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSynchronizeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSynchronize") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSynchronize(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSynchronize(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetConfFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetConf") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetConf(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetConf(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetTickResolutionFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetTickResolution") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetTickResolution(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetTickResolution(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetCurrentTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetCurrentTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetCurrentTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetCurrentTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtc_driver_17C26C00Function = new HLEModuleFunction("sceRtc_driver", "sceRtc_driver_17C26C00") {
		@Override
		public final void execute(Processor processor) {
			sceRtc_driver_17C26C00(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtc_driver_17C26C00(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetCurrentTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetCurrentTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetCurrentTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetCurrentTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtc_driver_B44BDAEDFunction = new HLEModuleFunction("sceRtc_driver", "sceRtc_driver_B44BDAED") {
		@Override
		public final void execute(Processor processor) {
			sceRtc_driver_B44BDAED(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtc_driver_B44BDAED(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetAccumulativeTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetAccumulativeTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetAccumulativeTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetAccumulativeTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtc_driver_029CA3B3Function = new HLEModuleFunction("sceRtc_driver", "sceRtc_driver_029CA3B3") {
		@Override
		public final void execute(Processor processor) {
			sceRtc_driver_029CA3B3(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtc_driver_029CA3B3(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetCurrentClockFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetCurrentClock") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetCurrentClock(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetCurrentClock(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetCurrentClockLocalTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetCurrentClockLocalTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetCurrentClockLocalTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetCurrentClockLocalTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcConvertUtcToLocalTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcConvertUtcToLocalTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcConvertUtcToLocalTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcConvertUtcToLocalTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcConvertLocalTimeToUTCFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcConvertLocalTimeToUTC") {
		@Override
		public final void execute(Processor processor) {
			sceRtcConvertLocalTimeToUTC(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcConvertLocalTimeToUTC(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetAlarmTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetAlarmTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetAlarmTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetAlarmTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetAlarmTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetAlarmTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetAlarmTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetAlarmTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcIsLeapYearFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcIsLeapYear") {
		@Override
		public final void execute(Processor processor) {
			sceRtcIsLeapYear(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcIsLeapYear(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetDaysInMonthFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetDaysInMonth") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetDaysInMonth(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetDaysInMonth(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetDayOfWeekFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetDayOfWeek") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetDayOfWeek(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetDayOfWeek(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcCheckValidFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcCheckValid") {
		@Override
		public final void execute(Processor processor) {
			sceRtcCheckValid(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcCheckValid(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetTime_tFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetTime_t") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetTime_t(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetTime_t(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetTime_tFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetTime_t") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetTime_t(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetTime_t(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetDosTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetDosTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetDosTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetDosTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetDosTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetDosTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetDosTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetDosTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetWin32FileTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetWin32FileTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetWin32FileTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetWin32FileTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetWin32FileTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetWin32FileTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetWin32FileTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetWin32FileTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcSetTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcSetTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcSetTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcSetTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcGetTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcGetTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcGetTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcGetTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcCompareTickFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcCompareTick") {
		@Override
		public final void execute(Processor processor) {
			sceRtcCompareTick(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcCompareTick(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddTicksFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddTicks") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddTicks(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddTicks(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddMicrosecondsFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddMicroseconds") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddMicroseconds(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddMicroseconds(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddSecondsFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddSeconds") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddSeconds(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddSeconds(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddMinutesFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddMinutes") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddMinutes(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddMinutes(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddHoursFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddHours") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddHours(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddHours(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddDaysFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddDays") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddDays(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddDays(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddWeeksFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddWeeks") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddWeeks(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddWeeks(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddMonthsFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddMonths") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddMonths(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddMonths(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcTickAddYearsFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcTickAddYears") {
		@Override
		public final void execute(Processor processor) {
			sceRtcTickAddYears(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcTickAddYears(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcFormatRFC2822Function = new HLEModuleFunction("sceRtc_driver", "sceRtcFormatRFC2822") {
		@Override
		public final void execute(Processor processor) {
			sceRtcFormatRFC2822(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcFormatRFC2822(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcFormatRFC2822LocalTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcFormatRFC2822LocalTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcFormatRFC2822LocalTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcFormatRFC2822LocalTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcFormatRFC3339Function = new HLEModuleFunction("sceRtc_driver", "sceRtcFormatRFC3339") {
		@Override
		public final void execute(Processor processor) {
			sceRtcFormatRFC3339(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcFormatRFC3339(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcFormatRFC3339LocalTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcFormatRFC3339LocalTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcFormatRFC3339LocalTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcFormatRFC3339LocalTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcParseDateTimeFunction = new HLEModuleFunction("sceRtc_driver", "sceRtcParseDateTime") {
		@Override
		public final void execute(Processor processor) {
			sceRtcParseDateTime(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcParseDateTime(processor);";
		}
	};
    
	public final HLEModuleFunction sceRtcParseRFC3339Function = new HLEModuleFunction("sceRtc_driver", "sceRtcParseRFC3339") {
		@Override
		public final void execute(Processor processor) {
			sceRtcParseRFC3339(processor);
		}
		@Override
		public final String compiledString() {
			return "jpcsp.HLE.Modules.sceRtc_driverModule.sceRtcParseRFC3339(processor);";
		}
	};
    
};
