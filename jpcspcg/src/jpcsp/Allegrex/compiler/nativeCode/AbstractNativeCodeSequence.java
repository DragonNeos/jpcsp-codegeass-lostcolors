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
package jpcsp.Allegrex.compiler.nativeCode;

import jpcsp.Memory;
import jpcsp.Allegrex.CpuState;
import jpcsp.Allegrex.compiler.RuntimeContext;
import jpcsp.memory.IMemoryReader;
import jpcsp.memory.MemoryReader;

/**
 * @author gid15
 *
 */
public abstract class AbstractNativeCodeSequence implements INativeCodeSequence {
	protected static char[] toUpperCase = buildToUpperCase();
	protected static char[] toLowerCase = buildToLowerCase();

	static char[] buildToUpperCase() {
		char[] toUpperCase = new char[256];
		for (int i = 0; i <= 0xFF; i++) {
			toUpperCase[i] = Character.toUpperCase((char) i);
		}

		return toUpperCase;
	}

	static char[] buildToLowerCase() {
		char[] toLowerCase = new char[256];
		for (int i = 0; i <= 0xFF; i++) {
			toLowerCase[i] = Character.toLowerCase((char) i);
		}

		return toLowerCase;
	}

	static protected int[] getGpr() {
		return RuntimeContext.gpr;
	}

	static protected CpuState getCpu() {
		return RuntimeContext.cpu;
	}

	static protected Memory getMemory() {
		return RuntimeContext.memory;
	}

	static protected int getRegisterValue(int register) {
		return getGpr()[register];
	}

	static protected long getLong(int low, int high) {
		return (((long) high) << 32) | low;
	}

	static protected int getGprA0() {
		return getGpr()[4];
	}

	static protected int getGprA1() {
		return getGpr()[5];
	}

	static protected int getGprA2() {
		return getGpr()[6];
	}

	static protected int getGprA3() {
		return getGpr()[7];
	}

	static protected int getGprT0() {
		return getGpr()[8];
	}

	static protected int getGprT1() {
		return getGpr()[9];
	}

	static protected int getGprT2() {
		return getGpr()[10];
	}

	static protected int getGprT3() {
		return getGpr()[11];
	}

	static protected int getStackParam0() {
		return getMemory().read32(getGprSp());
	}

	static protected int getStackParam1() {
		return getMemory().read32(getGprSp() + 4);
	}

	static protected int getStackParam2() {
		return getMemory().read32(getGprSp() + 8);
	}

	static protected int getGprSp() {
		return getGpr()[29];
	}

	static protected void setGprV0(int v0) {
		getGpr()[2] = v0;
	}

	static protected void setGprV0V1(long v0v1) {
		getGpr()[2] = (int) v0v1;
		getGpr()[3] = (int) (v0v1 >> 32);
	}

	static protected void setRegisterValue(int register, int value) {
		getGpr()[register] = value;
	}

	static float[] getFpr() {
		return getCpu().fpr;
	}

	static protected float getFprF12() {
		return getFpr()[12];
	}

	static protected void setFprF0(float f0) {
		getFpr()[0] = f0;
	}

	static protected float getFRegisterValue(int register) {
		return getFpr()[register];
	}

	static protected int getStrlen(int srcAddr) {
		int srcAddr3 = srcAddr & 3;
		// Reading 32-bit values is much faster
		IMemoryReader memoryReader = MemoryReader.getMemoryReader(srcAddr - srcAddr3, 4);
		if (memoryReader == null) {
			jpcsp.Allegrex.compiler.Compiler.log.warn("getStrlen: null MemoryReader");
			return 0;
		}

		int value;
		int offset = 0;
		switch (srcAddr3) {
			case 1:
				value = memoryReader.readNext();
				if ((value & 0x0000FF00) == 0) {
					return 0;
				}
				if ((value & 0x00FF0000) == 0) {
					return 1;
				}
				if ((value & 0xFF000000) == 0) {
					return 2;
				}
				offset = 3;
				break;
			case 2:
				value = memoryReader.readNext();
				if ((value & 0x00FF0000) == 0) {
					return 0;
				}
				if ((value & 0xFF000000) == 0) {
					return 1;
				}
				offset = 2;
				break;
			case 3:
				value = memoryReader.readNext();
				if ((value & 0xFF000000) == 0) {
					return 0;
				}
				offset = 1;
				break;
		}

		// Read 32-bit values and check for a null-byte
		while (true) {
			value = memoryReader.readNext();
			if ((value & 0x000000FF) == 0) {
				return offset;
			}
			if ((value & 0x0000FF00) == 0) {
				return offset + 1;
			}
			if ((value & 0x00FF0000) == 0) {
				return offset + 2;
			}
			if ((value & 0xFF000000) == 0) {
				return offset + 3;
			}
			offset += 4;
		}
	}

	static protected int getRelocatedAddress(int address1, int address2) {
		int address = (address1 << 16) + (short) address2;
		return address & Memory.addressMask;
	}
}
