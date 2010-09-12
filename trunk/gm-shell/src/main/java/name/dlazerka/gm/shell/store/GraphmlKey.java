package name.dlazerka.gm.shell.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphmlKey {
	private static final Logger logger = LoggerFactory.getLogger(GraphmlKey.class);

	private final String id;
	private final GraphmlKeyFor for_;
	private final String default_;
	private final String name;
	private final String type;

	protected GraphmlKey(String id, String default_, GraphmlKeyFor for_, String name, String type) {
		this.id = id;
		this.default_ = default_;
		this.for_ = for_;
		this.name = name;
		this.type = type;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((for_ == null) ? 0 : for_.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof GraphmlKey)) return false;
		GraphmlKey other = (GraphmlKey) obj;
		if (for_ == null) {
			if (other.for_ != null) return false;
		}
		else if (!for_.equals(other.for_)) return false;
		if (id == null) {
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GraphmlKey [");
		if (this.id != null) builder.append("id=").append(this.id).append(", ");
		if (this.name != null) builder.append("name=").append(this.name).append(", ");
		if (this.for_ != null) builder.append("for_=").append(this.for_).append(", ");
		if (this.default_ != null) builder.append("default_=").append(this.default_).append(", ");
		if (this.type != null) builder.append("type=").append(this.type);
		builder.append("]");
		return builder.toString();
	}

	public String getId() {
		return id;
	}

	public String getDefault() {
		return default_;
	}

	public GraphmlKeyFor getFor() {
		return this.for_;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

}
