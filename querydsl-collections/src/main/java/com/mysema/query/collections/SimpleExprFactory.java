/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang.StringUtils;

import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * SimpleExprFactory is a factory implementation for the creation of Path instances
 *
 * @author tiwe
 * @version $Id$
 */
// TODO : consider moving this later to querydsl-core
class SimpleExprFactory implements ExprFactory {
    
    private long counter = 0;
    
    private final PBoolean btrue = new PBoolean(md()), bfalse = new PBoolean(md());
    
    private final ExtString strExt = new ExtString(PathMetadata.forVariable("str"));
    
    private final Map<Object,PBooleanArray> baToPath = new PathFactory<Object,PBooleanArray>(new Transformer<Object,PBooleanArray>(){
        @SuppressWarnings("unchecked")
        public PBooleanArray transform(Object arg) {
            return new PBooleanArray(md());
        }    
    });
        
    private final Map<Object,PComparableArray<?>> caToPath = new PathFactory<Object,PComparableArray<?>>(new Transformer<Object,PComparableArray<?>>(){
        @SuppressWarnings("unchecked")
        public PComparableArray<?> transform(Object arg) {
            return new PComparableArray(((List)arg).get(0).getClass(), md());
        }    
    });
    
    private final Map<Object,PComparable<?>> comToPath = new PathFactory<Object,PComparable<?>>(new Transformer<Object,PComparable<?>>(){
        @SuppressWarnings("unchecked")
        public PComparable<?> transform(Object arg) {
            return new PComparable(arg.getClass(), md());
        }    
    });
    
    private final Map<Object,PStringArray> saToPath = new PathFactory<Object,PStringArray>(new Transformer<Object,PStringArray>(){
        public PStringArray transform(Object arg) {
            return new PStringArray(md());
        }    
    });
        
    private final Map<Object,PSimple<?>> simToPath = new PathFactory<Object,PSimple<?>>(new Transformer<Object,PSimple<?>>(){
        @SuppressWarnings("unchecked")
        public PSimple<?> transform(Object arg) {
            return new PSimple(arg.getClass(), md());
        }    
    });
    
    private final Map<String,ExtString> strToExtPath = new PathFactory<String,ExtString>(new Transformer<String,ExtString>(){
        public ExtString transform(String str) {
            return new ExtString(md());
        }        
    });
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.Boolean)
     */
    public PBoolean create(Boolean arg){
        return arg.booleanValue() ? btrue : bfalse;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.Boolean[])
     */
    public PBooleanArray create(Boolean[] args){
        return baToPath.get(Arrays.asList(args));
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D)
     */
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> PComparable<D> create(D arg){
        return (PComparable<D>) comToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D)
     */
    @SuppressWarnings("unchecked")
    public <D> PSimple<D> create(D arg){
        return (PSimple<D>) simToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D[])
     */
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> PComparableArray<D> create(D[] args){
        return (PComparableArray<D>) caToPath.get(Arrays.asList(args));
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.String)
     */
    public ExtString create(String arg){
        return StringUtils.isEmpty(arg) ? strExt : strToExtPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.String[])
     */
    public PStringArray create(String[] args){
        return saToPath.get(Arrays.asList(args));
    }
    
    private PathMetadata<String> md(){
        return PathMetadata.forVariable("v"+String.valueOf(++counter));
    }
    
    private static class PathFactory<K,V> extends LazyMap<K,V>{

        private static final long serialVersionUID = -2443097467085594792L;
        
        protected PathFactory(Transformer<K,V> transformer) {
            super(new WeakHashMap<K,V>(), transformer);
        }
                
    }
   
}
