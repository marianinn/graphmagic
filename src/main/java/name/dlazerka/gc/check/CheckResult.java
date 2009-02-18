package name.dlazerka.gc.check;

/**
 * @author Dzmitry Lazerka
 */
public class CheckResult {
	protected Boolean satisfy;

	public CheckResult(boolean satisfy) {
		this.satisfy = satisfy;
	}

	public Boolean getSatisfy() {
		return satisfy;
	}

	public void setSatisfy(Boolean satisfy) {
		this.satisfy = satisfy;
	}
}
