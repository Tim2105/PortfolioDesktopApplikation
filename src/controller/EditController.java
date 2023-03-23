package controller;

public abstract class EditController<T> extends Controller {

	protected T entity;
	
	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	public T getEntity() {
		return this.entity;
	}
	
}
