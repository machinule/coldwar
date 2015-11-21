package coldwar;

import java.util.concurrent.ConcurrentHashMap;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;

public class ComputationCache {
	
  public ComputationCache(GameState state, MoveList usa, MoveList ussr) {
	  this.intCache = new ConcurrentHashMap<IntegerComputation, Integer>();
	  this.state = state;
	  this.usa = usa;
	  this.ussr = ussr;
  }
	
  private ConcurrentHashMap<IntegerComputation, Integer> intCache;
  private GameState state;
  private MoveList usa;
  private MoveList ussr;
  
  public void clearCaches() {
	  intCache.clear();
  }
  
  public int computeInteger(IntegerComputation ic) {
	  return intCache.computeIfAbsent(ic, c -> c.compute(getState(), getUSAMove(), getUSSRMove()));
  }
  
  public GameState getState() {
	  return state;
  }
  
  public MoveList getUSAMove() {
	  return usa;
  }
  
  public MoveList getUSSRMove() {
	  return ussr;
  }
  
  public void setState(GameState state) {
	  this.state = state;
	  clearCaches();
  }
  
  public void setUSAMove(MoveList usa) {
	  this.usa = usa;
	  clearCaches();
  }
  
  public void setUSSRMove(MoveList ussr) {
	  this.ussr = ussr;
	  clearCaches();
  }
}