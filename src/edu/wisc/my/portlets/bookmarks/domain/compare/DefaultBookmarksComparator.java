/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.portlets.bookmarks.domain.compare;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import edu.wisc.my.portlets.bookmarks.domain.Bookmark;
import edu.wisc.my.portlets.bookmarks.domain.Entry;
import edu.wisc.my.portlets.bookmarks.domain.Folder;

/**
 * Implements the default comparison order for the objects involved in a BookmarkSet.
 * 
 * Comparison follows these rules in order:
 * Folders are always greater than non Folders.
 * Entry fields are compared in the following order: name, note, created, modified.
 * Bookmark fields are compared in the follwing order: 
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision$
 */
public class DefaultBookmarksComparator implements Comparator<Entry>, Serializable {
    public static DefaultBookmarksComparator DEFAULT_BOOKMARKS_COMPARATOR = new DefaultBookmarksComparator();
    
    protected DefaultBookmarksComparator() {
    }

    public int compare(Entry e1, Entry e2) {
        return new CompareToBuilder()
            .appendSuper(this.compareFolders(e1, e2))
            .appendSuper(this.compareEntries(e1, e2))
            .appendSuper(this.compareBookmarks(e1, e2))
            .toComparison();
    }

    /**
     * Folders are always greater than non-Folders, if they are both Folders
     * or both not Folders they are equal. 
     */
    protected int compareFolders(final Entry e1, final Entry e2) {
        final boolean f1 = e1 instanceof Folder;
        final boolean f2 = e2 instanceof Folder;
        
        if (f1 && !f2) {
            return -1;
        }
        else if (!f1 && f2) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Compairs the entries by name, note, created and modified properties in that
     * order.
     */
    protected int compareEntries(final Entry e1, final Entry e2) {
        return new CompareToBuilder()
            .append(e1.getName(), e2.getName())
            .append(e1.getNote(), e2.getNote())
            .append(e1.getCreated(), e2.getCreated())
            .append(e1.getModified(), e2.getModified())
            .toComparison();
    }

    /**
     * If both classes are not Bookmarks they are equal. If they are both
     * bookmarks they are compared by url then newWindow properties.
     */
    protected int compareBookmarks(final Entry e1, final Entry e2) {
        if (e1 instanceof Bookmark && e2 instanceof Bookmark) {
            final Bookmark b1 = (Bookmark)e1;
            final Bookmark b2 = (Bookmark)e2;

            return new CompareToBuilder()
                .append(b1.getUrl(), b2.getUrl())
                .append(b1.isNewWindow(), b2.isNewWindow())
                .toComparison();
        }
        else {
            return 0;
        }
    }
}