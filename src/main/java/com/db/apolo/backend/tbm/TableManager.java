package com.db.apolo.backend.tbm;

import com.db.apolo.backend.dm.DataManager;
import com.db.apolo.backend.parser.statement.Begin;
import com.db.apolo.backend.parser.statement.Create;
import com.db.apolo.backend.parser.statement.Delete;
import com.db.apolo.backend.parser.statement.Insert;
import com.db.apolo.backend.parser.statement.Select;
import com.db.apolo.backend.parser.statement.Update;
import com.db.apolo.backend.util.Parser;
import com.db.apolo.backend.vm.VersionManager;

public interface TableManager {
    BeginRes begin(Begin begin);

    byte[] commit(long xid) throws Exception;

    byte[] abort(long xid);

    byte[] show(long xid);

    byte[] create(long xid, Create create) throws Exception;

    byte[] insert(long xid, Insert insert) throws Exception;

    byte[] read(long xid, Select select) throws Exception;

    byte[] update(long xid, Update update) throws Exception;

    byte[] delete(long xid, Delete delete) throws Exception;

    static TableManager create(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.create(path);
        booter.update(Parser.long2Byte(0));
        return new TableManagerImpl(vm, dm, booter);
    }

    static TableManager open(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.open(path);
        return new TableManagerImpl(vm, dm, booter);
    }
}
