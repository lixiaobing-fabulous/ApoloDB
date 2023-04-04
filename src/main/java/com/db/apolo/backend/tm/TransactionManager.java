package com.db.apolo.backend.tm;

import com.db.apolo.backend.util.Panic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.db.apolo.common.Error.FileCannotRWException;
import static com.db.apolo.common.Error.FileExistsException;
import static com.db.apolo.common.Error.FileNotExistsException;

public interface TransactionManager {
    long begin();

    void commit(long xid);

    void abort(long xid);

    boolean isActive(long xid);

    boolean isCommitted(long xid);

    boolean isAborted(long xid);

    void close();

    public static TransactionManagerImpl create(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        try {
            if (!f.createNewFile()) {
                Panic.panic(FileExistsException);
            }
        } catch (Exception e) {
            Panic.panic(e);
        }
        if (!f.canRead() || !f.canWrite()) {
            Panic.panic(FileCannotRWException);
        }

        FileChannel      fc  = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (FileNotFoundException e) {
            Panic.panic(e);
        }

        // 写空XID文件头
        ByteBuffer buf = ByteBuffer.wrap(new byte[TransactionManagerImpl.LEN_XID_HEADER_LENGTH]);
        try {
            fc.position(0);
            fc.write(buf);
        } catch (IOException e) {
            Panic.panic(e);
        }

        return new TransactionManagerImpl(raf, fc);
    }

    public static TransactionManagerImpl open(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        if (!f.exists()) {
            Panic.panic(FileNotExistsException);
        }
        if (!f.canRead() || !f.canWrite()) {
            Panic.panic(FileCannotRWException);
        }

        FileChannel      fc  = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (FileNotFoundException e) {
            Panic.panic(e);
        }

        return new TransactionManagerImpl(raf, fc);
    }
}
