import java.util.Set;

import org.onebusaway.guice.jsr250.JSR250Module;
import org.onebusway.gtfs_realtime.exporter.GtfsRealtimeExporterModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class ExporterModule extends AbstractModule {

	public static void addModuleAndDependencies(Set<Module> modules) {
		modules.add(new ExporterModule());
		GtfsRealtimeExporterModule.addModuleAndDependencies(modules);
		JSR250Module.addModuleAndDependencies(modules);
	}

	@Override
	protected void configure() {
		bind(ExporterProviderImpl.class);
	}

	/**
	 * Implement hashCode() and equals() such that two instances of the module
	 * will be equal.
	 */
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		return this.getClass().equals(o.getClass());
	}
}