package com.qdb.provmgr.inter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by wenzhong on 2017/2/22.
 */
public class FileListFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return !pathname.isHidden();
    }
}
