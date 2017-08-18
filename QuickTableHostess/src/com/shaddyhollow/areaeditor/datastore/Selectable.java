package com.shaddyhollow.areaeditor.datastore;

public interface Selectable<K> {
	public void clearSelectionPosition();
	public void setSelection(K selection);
	public K getSelection();
}
