package basic.units;

import org.jlayer.annotations.*;

/**
 * 
 * unit for testing concurrency
 * 
 * @author gerd
 *
 */
@LayerUnit
public class Unit3 {
	
	public Unit3(){ noItem = 4711; }
	public Unit3(int i){ noItem = i; }
	
	public int noItem;
	public int x1 = 0;
	public int x2 = 0;
	public int x3 = 0;
	
	public long threadId;
	public int cnt;
	
	@LayerField
	public Unit3[] unit3Vector;
	
	/**********************************************************************************/
	/**    void(), void(int), void(@LayerParam int), void(Unit2, @LayerParam Unit2)  **/
	/**********************************************************************************/
	
	// void()
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidMethod_fork(){
		threadId = Thread.currentThread().getId();
		cnt = 0;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidMethod_sequ(){
		threadId = Thread.currentThread().getId();
		cnt = 0;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(int)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidInt_fork(int intPar){
		threadId = Thread.currentThread().getId();
		cnt = intPar;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidInt_sequ(int intPar){
		threadId = Thread.currentThread().getId();
		cnt = intPar;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(@LayerParam int)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidLPInt_fork(@LayerParam int intPar){
		threadId = Thread.currentThread().getId();
		cnt = intPar;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidLPInt_sequ(@LayerParam int intPar){
		threadId = Thread.currentThread().getId();
		cnt = intPar;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(Unit2, @LayerParam Unit2)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidUnit2LPUnit2_fork(Unit2 unit2, @LayerParam Unit2 lunit2){
		threadId = Thread.currentThread().getId();
		cnt = unit2.noItem + lunit2.noItem;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidUnit2LPUnit2_sequ(Unit2 unit2, @LayerParam Unit2 lunit2){
		threadId = Thread.currentThread().getId();
		cnt = unit2.noItem + lunit2.noItem;
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
//	/**********************************************************************************/
//	/**    int(), int(int), int(@LayerParam int), Unit2(Unit2, @LayerParam Unit2)    **/
//	/**********************************************************************************/
	
	// int()
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public int intMethod_fork(){
		threadId = Thread.currentThread().getId();
		int cnt = 0;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	@LayerMethod
	public int intMethod_sequ(){
		threadId = Thread.currentThread().getId();
		int cnt = 0;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	// int(int)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public int intInt_fork(int intPar){
		threadId = Thread.currentThread().getId();
		int cnt = intPar;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	@LayerMethod
	public int intInt_sequ(int intPar){
		threadId = Thread.currentThread().getId();
		int cnt = intPar;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	// int(@LayerParam int)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public int intLPInt_fork(@LayerParam int intPar){
		threadId = Thread.currentThread().getId();
		int cnt = intPar;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	@LayerMethod
	public int intLPInt_sequ(@LayerParam int intPar){
		threadId = Thread.currentThread().getId();
		int cnt = intPar;
		if (unit3Vector==null) return 0;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return cnt;
	}
	
	// Unit2(Unit2, @LayerParam Unit2)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public Unit2 unit2Unit2LPUnit2_fork(Unit2 unit2, @LayerParam Unit2 lpunit2){
		threadId = Thread.currentThread().getId();
		int cnt = unit2.noItem + lpunit2.noItem;
		if (unit3Vector==null) return null;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return new Unit2(cnt);
	}
	
	@LayerMethod
	public Unit2 unit2Unit2LPUnnit2_sequ(Unit2 unit2, @LayerParam Unit2 lpunit2){
		threadId = Thread.currentThread().getId();
		int cnt = unit2.noItem + lpunit2.noItem;
		if (unit3Vector==null) return null;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
		return new Unit2(cnt);
	}
	
//	/*******************************************************************************************/
//	/**   void(int...), void(@LayerParam int...), void(Unit2...), void(@LayerParam Unit2...)  **/
//	/*******************************************************************************************/
	
	// void(int...)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidIntDot_fork(int... intPar){
		threadId = Thread.currentThread().getId();
		int[] par = intPar;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i];
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidIntDot_sequ(int... intPar){
		threadId = Thread.currentThread().getId();
		int[] par = intPar;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i];
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(@LayerParam int...)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidLPIntDot_fork(@LayerParam int... intPar){
		threadId = Thread.currentThread().getId();
		int[] par = intPar;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i];
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidLPIntDot_sequ(@LayerParam int... intPar){
		threadId = Thread.currentThread().getId();
		int[] par = intPar;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i];
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(Unit2...)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidUnit2Dot_fork(Unit2... unit2){
		threadId = Thread.currentThread().getId();
		Unit2[] par = unit2;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i].noItem;
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidUnit2Dot_sequ(Unit2... unit2){
		threadId = Thread.currentThread().getId();
		Unit2[] par = unit2;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i].noItem;
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	// void(@LayerParam Unit2...)
	
	@LayerMethod(runtimeMode=RuntimeMode.FORKJOIN)
	public void voidLPUnit2Dot_fork(@LayerParam Unit2... lunit2){
		threadId = Thread.currentThread().getId();
		Unit2[] par = lunit2;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i].noItem;
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	@LayerMethod
	public void voidLPUnit2Dot_sequ(@LayerParam Unit2... lunit2){
		threadId = Thread.currentThread().getId();
		Unit2[] par = lunit2;
		cnt = 0;
		for (int i = 0; i < par.length; i++) {
			cnt += par[i].noItem;
		}
		if (unit3Vector==null) return;
		for (Unit3 unit3 : unit3Vector) {
			cnt += unit3.x1 + unit3.x2 + unit3.x3;
		}
	}
	
	
}
