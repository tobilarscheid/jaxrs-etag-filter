package de.tobiaslarscheid.etag;

//let's introduce some issues
public class BadClass {

	public BadClass() {

	}

	public void doUselessSysos() {
		System.out.println("duplicate");
		System.out.println("very very long strict that should better life somewhere else then here");
		System.out.println("duplicate");
		System.out.println("duplicate");
		System.out.println("duplicate");
	}

	public void duplicateMethod() {
		System.out.println("duplicate");
		System.out.println("very very long strict that should better life somewhere else then here");
		System.out.println("duplicate");
		System.out.println("duplicate");
		System.out.println("duplicate");
	}

	public void uselessTryCatchStuff() {
		try {
			throw new Exception();
		} catch (Exception e) {
			// Do nothing!
		}
	}

}
