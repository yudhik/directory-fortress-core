package com.jts.fortress.util.cache;

import com.jts.fortress.rbac.SDSet;

/**
 * Value object wraps {@link SDSet} for caching purposes.  This object also provides attributes that are used for
 * searching the DSD cache.
 *
 * @author Shawn McKinney
 * @created June 2, 2012
 */
public class DsdCacheEntry
{
    private String name;
    private String member;
    private SDSet sdSet;
    private boolean empty;

    /**
     * Non-default contructor takes a {@link DsdCacheEntry#member} that maps to  {@link com.jts.fortress.rbac.Role#name}, along
     * with a reference to {@link SDSet} and a boolean value to indicate if DSD not found for member, which indicates empty.
     *
     * @param member maps to {@link com.jts.fortress.rbac.Role#name}
     * @param sdSet contains DSD entry.
     * @param empty if true, the DSD entry was not found for a corresponding Role.
     */
    public DsdCacheEntry(String member, SDSet sdSet, boolean empty)
    {
        this.sdSet = sdSet;
        this.member = member;
        this.empty = empty;
    }

    /**
     * To prevent repeated reads of the DSD's in the directory, Entries are added to the cache and marked empty.
     *
     * @return boolean if cache entry is 'empty'.
     */
    public boolean isEmpty()
    {
        return empty;
    }

    /**
     * To prevent repeated reads of the DSD's in the directory, Entries are added to the cache and marked empty.
     *
     * @param empty if the cache entry is 'empty'.
     */
    public void setEmpty(boolean empty)
    {
        this.empty = empty;
    }

    /**
     * Return the Role name this cache entry corresponds with.
     *
     * @return Role name.
     */
    public String getMember()
    {
        return member;
    }

    /**
     * Set the Role name this cache entry corresponds with.
     *
     * @param member Role name
     */
    public void setMember(String member)
    {
        this.member = member;
    }

    /**
     * Get the DSD for this cache entry.
     *
     * @return {@link SDSet} of type {@link SDSet.SDType#DYNAMIC}.
     */
    public SDSet getSdSet()
    {
        return sdSet;
    }

    /**
     * Set the DSD for this cache entry.
     *
     * @param sdSet reference to non-null {@link SDSet} of type {@link SDSet.SDType#DYNAMIC}.
     */
    public void setSdSet(SDSet sdSet)
    {
        this.sdSet = sdSet;
    }

    /**
     * Return the name that is used as the cache name for this entry.
     *
     * @return name for the Cache entry.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name to be used for this cache entry.
     *
     * @param name of the cache entry.
     */
    public void setName(String name)
    {
        this.name = name;
    }
}