package io.github.a13e300.tools.copy_sign.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SignCopy {
    public static void copySign(File src, File dst) throws Throwable {
        File tmp = File.createTempFile(src.getName(), ".tmp");
        try (var srcFile = new RandomAccessFile(src, "r")) {
            var srcInfo = ApkSigningBlockUtils.findSignBlock(srcFile);
            try (var dstFile = new RandomAccessFile(dst, "r")) {
                var dstInfo = ApkSigningBlockUtils.findSignBlock(dstFile);
                try (var tmpFile = new RandomAccessFile(tmp, "rw")) {
                    copySlice(dstFile, 0, tmpFile, 0, (int) dstInfo.sbOffset);
                    copySlice(srcFile, srcInfo.sbOffset, tmpFile, dstInfo.sbOffset, (int) srcInfo.sbSize);
                    copySlice(dstFile, dstInfo.cdOffset, tmpFile, dstInfo.sbOffset + srcInfo.sbSize, (int) (dstFile.length() - dstInfo.cdOffset));
                    var newOffsetOfCdOffset = dstInfo.offsetOfCdOffset - dstInfo.sbSize + srcInfo.sbSize;
                    var newCdOffset = dstInfo.cdOffset - dstInfo.sbSize + srcInfo.sbSize;
                    var buf = ByteBuffer.allocate(4);
                    buf.order(ByteOrder.LITTLE_ENDIAN);
                    buf.putInt((int) newCdOffset);
                    tmpFile.seek(newOffsetOfCdOffset);
                    tmpFile.write(buf.array(), 0, buf.capacity());
                }
            }
            dst.delete();
            Files.copy(Paths.get(tmp.getAbsolutePath()), Paths.get(dst.getAbsolutePath()));
        } finally {
            tmp.delete();
        }
    }

    static void copySlice(RandomAccessFile src, long srcOff, RandomAccessFile dst, long dstOff, int size) throws Throwable {
        byte[] buf = new byte[4096];
        src.seek(srcOff);
        dst.seek(dstOff);
        for (int i = 0; i < size; i += buf.length) {
            var realSize = Math.min(buf.length, size - i);
            src.readFully(buf, 0, realSize);
            dst.write(buf, 0, realSize);
        }
    }
}
