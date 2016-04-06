package org.qfox.jestful.server.tree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.qfox.jestful.server.exception.AlreadyValuedException;

/**
 * <p>
 * Description: 资源节点
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年3月31日 下午4:14:03
 *
 * @since 1.0.0
 */
public class Node<K extends Expression<K>, V extends Object> implements Comparable<Node<K, V>> {
	protected final K key;
	protected final String separator;
	protected final Set<Node<K, V>> branches = new TreeSet<Node<K, V>>();
	protected V value;

	public Node(K key) {
		this(key, '/');
	}

	public Node(K key, char separator) {
		if (key == null) {
			throw new IllegalArgumentException("node key may not be null");
		}
		this.key = key;
		this.separator = String.valueOf(separator);
	}

	/**
	 * 返回匹配指定路径的所有节点
	 * 
	 * @param uri
	 *            路径
	 * @return 所有匹配的节点
	 */
	public Set<Node<K, V>> match(String uri) {
		String[] hierarchies = uri.split(separator);
		Iterator<String> iterator = Arrays.asList(hierarchies).iterator();
		String path = iterator.next();
		while (path.isEmpty() && iterator.hasNext()) {
			path = iterator.next();
		}
		Set<Node<K, V>> nodes = new TreeSet<Node<K, V>>();
		if (key.match(path) == false) {
			return nodes;
		}
		if (iterator.hasNext()) {
			if (this.isLeaf()) {
				return nodes;
			}
			StringBuilder builder = new StringBuilder();
			while (iterator.hasNext()) {
				builder.append(separator);
				builder.append(iterator.next());
			}
			for (Node<K, V> branch : branches) {
				Set<Node<K, V>> subnodes = branch.match(builder.toString());
				nodes.addAll(subnodes);
			}
		} else {
			nodes.add(this);
		}
		return nodes;
	}

	/**
	 * 合并另一个节点
	 * 
	 * @param node
	 *            子节点
	 */
	public void merge(Node<K, V> node) throws AlreadyValuedException {
		if (this.value != null && node.value != null) {
			throw new AlreadyValuedException(node, this);
		}

		if (node.value != null) {
			this.value = node.value;
		}

		Set<Node<K, V>> branches = new TreeSet<Node<K, V>>();
		flag: for (Node<K, V> nodebranch : node.branches) {
			for (Node<K, V> thisbranch : this.branches) {
				if (thisbranch.equals(nodebranch)) {
					thisbranch.merge(nodebranch);
					continue flag;
				}
			}
			branches.add(nodebranch);
		}

		this.branches.addAll(branches);
	}

	public boolean isLeaf() {
		return branches.isEmpty();
	}

	public boolean isValued() {
		return value != null;
	}

	public K getKey() {
		return key;
	}

	public String getSeparator() {
		return separator;
	}

	public Set<Node<K, V>> getBranches() {
		return branches;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int compareTo(Node<K, V> node) {
		return this.key.compareTo(node.key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node<?, ?> other = (Node<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.toString("");
	}

	public String toString(String prefix) {
		StringBuilder builder = new StringBuilder();
		builder.append(separator + key).append(value != null ? " " + value.toString() : "").append("\r\n");
		if (this.isLeaf() == false) {
			Iterator<Node<K, V>> iterator = branches.iterator();
			while (iterator.hasNext()) {
				Node<K, V> branch = iterator.next();
				builder.append(prefix).append("|\r\n");
				builder.append(prefix).append(iterator.hasNext() ? "|" : "\\").append("--");
				builder.append(branch.toString(prefix + (iterator.hasNext() ? "|  " : "   ")));
			}
		}
		return builder.toString();
	}

}
