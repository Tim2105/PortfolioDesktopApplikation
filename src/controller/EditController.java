package controller;

public abstract class EditController<T> extends Controller {

	protected T entity;
	
	public void setEntity(T entity) {
		this.entity = entity;
		this.update();
	}
	
	public T getEntity() {
		return this.entity;
	}
	
	protected abstract void update();
	
}
