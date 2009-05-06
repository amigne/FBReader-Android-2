/*
 * Copyright (C) 2007-2009 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.zlibrary.core.filesystem;

import java.util.*;

import org.geometerplus.zlibrary.core.filesystem.tar.ZLTarEntryFile;

public abstract class ZLArchiveEntryFile extends ZLFile {
	public static ZLArchiveEntryFile createArchiveEntryFile(ZLFile archive, String entryName) {
		switch (archive.myArchiveType & ArchiveType.ARCHIVE) {
			case ArchiveType.ZIP: 
				return new ZLZipEntryFile(archive, entryName);
			case ArchiveType.TAR: 
				return new ZLTarEntryFile(archive, entryName);
			default:
				return null;
		}
	}

	static List<ZLFile> archiveEntries(ZLFile archive) {
		switch (archive.myArchiveType & ArchiveType.ARCHIVE) {
			case ArchiveType.ZIP:
				return ZLZipEntryFile.archiveEntries(archive);
			case ArchiveType.TAR:
				return ZLTarEntryFile.archiveEntries(archive);
			default:
				return Collections.emptyList();
		}
	}

	protected final ZLFile myParent;
	protected final String myName;
	private String myShortName;
	
	protected ZLArchiveEntryFile(ZLFile parent, String name) {
		myParent = parent;
		myName = name;
		init();
	}
	
	@Override
	public boolean exists() {
		return myParent.exists();
	}
	
	@Override
	public boolean isDirectory() {
		return false;
	}
	
	@Override
	public String getPath() {
		return myParent.getPath() + ":" + myName;
	}
	
	@Override
	public String getNameWithExtension() {
		if (myShortName == null) {
			final String name = myName;
			final int index = name.lastIndexOf('/');
			if (index == -1) {
				myShortName = name;
			} else {
				myShortName = name.substring(index + 1);
			}
		}
		return myShortName;
	}

	@Override
	public ZLFile getParent() {
		return myParent;
	}

	@Override
	public ZLPhysicalFile getPhysicalFile() {
		ZLFile ancestor = myParent;
		while ((ancestor != null) && !(ancestor instanceof ZLPhysicalFile)) {
			ancestor = ancestor.getParent();
		}
		return (ZLPhysicalFile)ancestor;
	}
}
