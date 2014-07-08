package com.darkdensity.util;
/**
 * 
* @ClassName: Pair
* @Description: A pair data structure
* @author Team A1 - Han
* @date Mar 28, 2014 6:44:03 AM
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