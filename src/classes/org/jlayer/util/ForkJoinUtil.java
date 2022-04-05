package org.jlayer.util;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

import org.jlayer.model.*;

/**
 * 
 * This class establishes the base for the parallel implementation of generated
 * {@link org.jlayer.annotations.LayerMethod LayerMethods} and
 * {@link VectorLayer VectorLayer's} special methods for connecting or associating layers.
 * <p>
 * The nested class {@link LayerAction LayerAction} use the <strong>Fork/Join Framework</strong>
 * as presented in the Tutorial
 * <a href="http://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html">  <strong>Fork/Join</strong></a>.
 * It extends {@link java.util.concurrent.RecursiveAction} and has two constructors.
 * Accordingly, the overriding method {@link LayerAction#compute() compute()} has 
 * two branches, which depend on the constructor having been chosen to create an object:
 * </p>
 * <ul>
 *   <li> <code>LayerAction(ActionItem <strong>actionItem</strong>)</code> 
 *         ==&gt; the given <strong>actionItem</strong> is computed directly
 *   <li> <code>LayerAction(List&lt;ActionItem&gt; <strong>actionList</strong>)</code> 
 *         ==&gt; the items of the given <strong>actionList</strong> are computed recursively 
 * </ul>
 * <p>
 * The nested class {@link ActionItem ActionItem} serves as a helper class to store
 * "<strong>compute()-objects</strong>", and the method {@link #getX1Slices(int x1Length) getX1Slices()}
 * can be used to establish lists of <strong>ActionItems</strong>.
 * </p>
 * <p>
 * A once generated <strong>layerAction</strong> can be executed by calling <strong>invoke()</strong>:
 * </p>
 * <ul>
 *    <li> <code>ForkJoinImpl.invoke(<strong>layerAction</strong>);</code>
 * </ul>
 * 
 * @see AssociateUtil
 * @see ConnectUtil
 * @see MethodUtil
 * 
 * @author Gerd Kock
 *
 */
public class ForkJoinUtil {
	
	// ForkJoinPool
	static private ForkJoinPool pool = null;
	
	/**
	 * establishes the ForkJoinPool - iff not yet present - and invokes the given task.
	 * 
	 * @param <T> the type of the task's result
	 * @param task the task
	 * @return the taks's result
	 */
	static public <T> T invoke(ForkJoinTask<T> task) {
		if (pool == null) pool = new ForkJoinPool();
		return pool.invoke(task);
	}

	// ActionItem
	public static abstract class ActionItem {
		
		abstract protected void compute();
		
	}
	
	// LayerAction
	static public class LayerAction extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		
		public LayerAction(ActionItem actionItem) {
			this.actionItem = actionItem;
		}
		public LayerAction(List<ActionItem> itemList) {
			this.itemList = itemList;
		}
		
		ActionItem actionItem = null;
		List<ActionItem> itemList = null;
		
		@Override
		protected void compute() {
			if (actionItem != null) {
				actionItem.compute();
			} else if (itemList != null) {
				computeRecursively(itemList);
			} else {
				throw new JLayerException();
			}
		}
		
		void computeRecursively(List<ActionItem> itemList) {
			List<LayerAction> actionList = new ArrayList<LayerAction>();
			for (ActionItem actionItem : itemList) {
				actionList.add(new LayerAction(actionItem));
			 }
			 invokeAll(actionList);
		}
		
	}
	
	/**
	 * 
	 * a tuple for representing slices
	 *
	 */
	static class Tuple {
		int low;
		int high;
	}
	
	/**
	 * separates the range with length <code>x1Length</code> into a number of slices.
	 * <p>
	 * The number of resulting slices is given by 
	 * <code>Runtime.getRuntime().availableProcessors()</code>.
	 * </p>
	 * Example:
	 * <ul>
	 *   <li> <code>"x1Length == 11"</code> ==&gt; given range is <code>[0..10]</code>
	 *   <li> <code>Runtime.getRuntime().availableProcessors() == 3</code>
	 *   <li> returns <code>[ [0,4], [4,8], [8,11] ]</code>
	 * </ul>
	 * 
	 * @param x1Length the upper bound of the range
	 * @return an array of {@link Tuple Tuples} describing the separation
	 */
	static Tuple[] getX1Slices(int x1Length) {
		int procNo = Runtime.getRuntime().availableProcessors();
		int quot = x1Length / procNo;
		int rest = x1Length % procNo;
		int[] x1Range = new int[procNo];
		for (int i = 0; i < procNo; i++) {
			x1Range[i] = quot;
		}
		for (int i = 0; i < rest; i++) {
			x1Range[i]++;
		}
		Tuple[] x1Slices = new Tuple[procNo];
		for (int i = 0; i < x1Slices.length; i++) {
			x1Slices[i] = new Tuple();
		}
		int low, high = 0;
		for (int i = 0; i < procNo; i++) {
			low = high;
			high = low + x1Range[i];
			x1Slices[i].low = low;
			x1Slices[i].high = high;
		}
		return x1Slices;
	}

}
