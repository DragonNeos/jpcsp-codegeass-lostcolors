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
package jpcsp.HLE.modules271;

import static jpcsp.Allegrex.Common._a0;
import static jpcsp.Allegrex.Common._a1;
import static jpcsp.Allegrex.Common._a2;
import static jpcsp.Allegrex.Common._a3;
import jpcsp.HLE.HLEFunction;
import jpcsp.Processor;
import jpcsp.HLE.kernel.Managers;

public class ThreadManForUser extends jpcsp.HLE.modules150.ThreadManForUser {
    @HLEFunction(nid = 0x0DDCD2C9, version = 271, checkInsideInterrupt = true)
    public void sceKernelTryLockMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelTryLockMutex(gpr[_a0], gpr[_a1]);
    }

    @HLEFunction(nid = 0x5BF4DD27, version = 271, checkInsideInterrupt = true, checkDispatchThreadEnabled = true)
    public void sceKernelLockMutexCB(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelLockMutexCB(gpr[_a0], gpr[_a1], gpr[_a2]);
    }

    @HLEFunction(nid = 0x6B30100F, version = 271, checkInsideInterrupt = true)
    public void sceKernelUnlockMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelUnlockMutex(gpr[_a0], gpr[_a1]);
    }

    @HLEFunction(nid = 0x87D9223C, version = 271)
    public void sceKernelCancelMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelCancelMutex(gpr[_a0], gpr[_a1], gpr[_a2]);
    }

    @HLEFunction(nid = 0xA9C2CB9A, version = 271)
    public void sceKernelReferMutexStatus(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelReferMutexStatus(gpr[_a0], gpr[_a1]);
    }

    @HLEFunction(nid = 0xB011B11F, version = 271, checkInsideInterrupt = true, checkDispatchThreadEnabled = true)
    public void sceKernelLockMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelLockMutex(gpr[_a0], gpr[_a1], gpr[_a2]);
    }

    @HLEFunction(nid = 0xB7D098C6, version = 271, checkInsideInterrupt = true)
    public void sceKernelCreateMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelCreateMutex(gpr[_a0], gpr[_a1], gpr[_a2], gpr[_a3]);
    }

    @HLEFunction(nid = 0xF8170FBE, version = 271, checkInsideInterrupt = true)
    public void sceKernelDeleteMutex(Processor processor) {
        int[] gpr = processor.cpu.gpr;
        Managers.mutex.sceKernelDeleteMutex(gpr[_a0]);
    }
}