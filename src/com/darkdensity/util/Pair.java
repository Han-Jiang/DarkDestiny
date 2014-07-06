package com.darkdensity.util;
/**
 * 
* @ClassName: Pair
* @Description: A pair class
* @author Han
* @date 2014年 下午3:35:37
* @param <FST> 
* @param <SND>
 */
public class Pair<FST,SND> {

  public final FST fst;
  public final SND snd;

  public Pair(FST fst, SND snd) {
    this.fst = fst;
    this.snd = snd;
  }

  @Override
  public int hashCode() { return fst.hashCode() ^ snd.hashCode(); }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.fst.equals(pairo.fst) &&
           this.snd.equals(pairo.snd);
  }

}