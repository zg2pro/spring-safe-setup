/*
 * The MIT License
 *
 * Copyright 2017 zg2pro.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.zg2pro.spring.safe.setup.fs;

import java.io.File;
import java.nio.file.FileSystemException;

/**
 *
 * @author zg2pro
 */
public class FsReady {

    protected void checkFileSystemIsReady(long necessaryDiskSpace, File... pathsToCheck) throws FileSystemException {
        for (File path : pathsToCheck) {
            if (!path.canRead()) {
                throw new FileSystemException("trouble with " + path.getAbsolutePath() + " - this path is locked for reading, please change path permissions");
            }
            if (!path.canWrite()) {
                throw new FileSystemException("trouble with " + path.getAbsolutePath() + " - this path is locked for writing, please change path permissions");
            }
            if (path.getFreeSpace() < necessaryDiskSpace) {
                throw new FileSystemException("trouble with " + path.getAbsolutePath() + " - the Application can't run with such a short disk space, "
                        + "please reallocate space on disk partition or free disk space");
            }
        }
    }
    
}
