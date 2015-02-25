/**
 * Copyright (C) 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.onebusaway.cli.CommandLineInterfaceLibrary;
import org.onebusaway.guice.jsr250.LifecycleService;
import org.onebusway.gtfs_realtime.exporter.AlertsFileWriter;
import org.onebusway.gtfs_realtime.exporter.AlertsServlet;
import org.onebusway.gtfs_realtime.exporter.TripUpdatesFileWriter;
import org.onebusway.gtfs_realtime.exporter.TripUpdatesServlet;
import org.onebusway.gtfs_realtime.exporter.VehiclePositionsFileWriter;
import org.onebusway.gtfs_realtime.exporter.VehiclePositionsServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ExporterMain {

	private final static String DEFAULT_TRIP_UPDATES_URL = "http://localhost:8080/trip-updates";

	private final static String DEFAULT_VEHICLE_POSITIONS_URL = "http://localhost:8080/vehicle-positions";

	private final static String DEFAULT_ALERTS_URL = "http://localhost:8080/alerts";

	private final static String DEFAULT_TRIP_UPDATES_PATH_READ = "F_TRIP";

	private final static String DEFAULT_VEHICLE_POSITIONS_PATH_READ = "F_VEHICLE";

	private final static String DEFAULT_ALERTS_PATH_READ = "F_ALERT";

	private static final String ARG_TRIP_UPDATES_PATH = "tripUpdatesPath";

	private static final String ARG_TRIP_UPDATES_URL = "tripUpdatesUrl";

	private static final String ARG_TRIP_UPDATES_PATH_READ = "tripUpdatesPathRead";

	private static final String ARG_VEHICLE_POSITIONS_PATH = "vehiclePositionsPath";

	private static final String ARG_VEHICLE_POSITIONS_URL = "vehiclePositionsUrl";

	private static final String ARG_VEHICLE_POSITIONS_PATH_READ = "vehiclePositionsPathRead";

	private static final String ARG_ALERTS_PATH = "alertsPath";

	private static final String ARG_ALERTS_URL = "alertsUrl";

	private static final String ARG_ALERTS_PATH_READ = "alertsPathRead";

	private LifecycleService lifecycleService;

	private ExporterProviderImpl provider;

	@Inject
	public void setProvider(ExporterProviderImpl provider) {
		this.provider = provider;
	}

	@Inject
	public void setLifecycleService(LifecycleService lifecycleService) {
		this.lifecycleService = lifecycleService;
	}

	public static void main(String[] args) throws Exception {
		ExporterMain m = new ExporterMain();
		m.run(args);
	}

	public void run(String[] args) throws Exception {

		if (CommandLineInterfaceLibrary.wantsHelp(args)) {
			printUsage();
			System.exit(-1);
		}

		Options options = new Options();
		buildOptions(options);
		Parser parser = new GnuParser();
		CommandLine cli = parser.parse(options, args);

		Set<Module> modules = new HashSet<Module>();
		ExporterModule.addModuleAndDependencies(modules);

		Injector injector = Guice.createInjector(modules);
		injector.injectMembers(this);

		provider.setTrip_update_path(DEFAULT_TRIP_UPDATES_PATH_READ);
		provider.setVehicle_positions_path(DEFAULT_VEHICLE_POSITIONS_PATH_READ);
		provider.setAlerts_path(DEFAULT_ALERTS_PATH_READ);

		URL urlTripUpdates = new URL(DEFAULT_TRIP_UPDATES_URL);
		URL urlVehiclePositions = new URL(DEFAULT_VEHICLE_POSITIONS_URL);
		URL urlAlerts = new URL(DEFAULT_ALERTS_URL);

		if (cli.hasOption(ARG_TRIP_UPDATES_URL)) {
			urlTripUpdates = new URL(cli.getOptionValue(ARG_TRIP_UPDATES_URL));
		}
		if (cli.hasOption(ARG_TRIP_UPDATES_PATH)) {
			File path = new File(cli.getOptionValue(ARG_TRIP_UPDATES_PATH));
			TripUpdatesFileWriter writer = injector.getInstance(TripUpdatesFileWriter.class);
			writer.setPath(path);
		}
		if (cli.hasOption(ARG_TRIP_UPDATES_PATH_READ)) {
			provider.setTrip_update_path(cli.getOptionValue(ARG_TRIP_UPDATES_PATH_READ));
		}

		if (cli.hasOption(ARG_VEHICLE_POSITIONS_URL)) {
			urlVehiclePositions = new URL(cli.getOptionValue(ARG_VEHICLE_POSITIONS_URL));
		}
		if (cli.hasOption(ARG_VEHICLE_POSITIONS_PATH)) {
			File path = new File(cli.getOptionValue(ARG_VEHICLE_POSITIONS_PATH));
			VehiclePositionsFileWriter writer = injector
					.getInstance(VehiclePositionsFileWriter.class);
			writer.setPath(path);
		}
		if (cli.hasOption(ARG_VEHICLE_POSITIONS_PATH_READ)) {
			provider.setVehicle_positions_path(cli.getOptionValue(ARG_VEHICLE_POSITIONS_PATH_READ));
		}

		if (cli.hasOption(ARG_ALERTS_URL)) {
			urlAlerts = new URL(cli.getOptionValue(ARG_ALERTS_URL));
		}
		if (cli.hasOption(ARG_ALERTS_PATH)) {
			File path = new File(cli.getOptionValue(ARG_ALERTS_PATH));
			AlertsFileWriter writer = injector.getInstance(AlertsFileWriter.class);
			writer.setPath(path);
		}
		if (cli.hasOption(ARG_ALERTS_PATH_READ)) {
			provider.setAlerts_path(cli.getOptionValue(ARG_ALERTS_PATH_READ));
		}

		TripUpdatesServlet tripUpdatesServlet = injector.getInstance(TripUpdatesServlet.class);
		tripUpdatesServlet.setUrl(urlTripUpdates);

		VehiclePositionsServlet vehiclePositionsServlet = injector
				.getInstance(VehiclePositionsServlet.class);
		vehiclePositionsServlet.setUrl(urlVehiclePositions);
		
		AlertsServlet alertsServlet = injector.getInstance(AlertsServlet.class);
		alertsServlet.setUrl(urlAlerts);

		lifecycleService.start();
	}

	private void printUsage() {
		CommandLineInterfaceLibrary.printUsage(getClass());
	}

	protected void buildOptions(Options options) {
		options.addOption(ARG_TRIP_UPDATES_PATH, true, "trip updates path");
		options.addOption(ARG_TRIP_UPDATES_URL, true, "trip updates url");
		options.addOption(ARG_TRIP_UPDATES_PATH_READ, true, "trip updates path to read");
		options.addOption(ARG_VEHICLE_POSITIONS_PATH, true, "vehicle positions path");
		options.addOption(ARG_VEHICLE_POSITIONS_URL, true, "vehicle positions url");
		options.addOption(ARG_VEHICLE_POSITIONS_PATH_READ, true, "vehicle positions path to read");
		options.addOption(ARG_ALERTS_PATH, true, "alerts path");
		options.addOption(ARG_ALERTS_URL, true, "alerts url");
		options.addOption(ARG_ALERTS_PATH_READ, true, "alerts path to read");

	}
}