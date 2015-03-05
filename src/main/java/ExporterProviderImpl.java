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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.onebusway.gtfs_realtime.exporter.GtfsRealtimeLibrary;
import org.onebusway.gtfs_realtime.exporter.GtfsRealtimeMutableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.transit.realtime.GtfsRealtime.FeedMessage;

@Singleton
public class ExporterProviderImpl {

	private static final Logger _log = LoggerFactory.getLogger(ExporterProviderImpl.class);

	private ScheduledExecutorService _executor;

	private GtfsRealtimeMutableProvider gtfsRealtimeProvider;

	private String trip_update_path;

	private String vehicle_positions_path;

	private String alerts_path;

	/**
	 * How often feed data will be read, in seconds.
	 */
	private int _refreshInterval = 7;

	@Inject
	public void setGtfsRealtimeProvider(GtfsRealtimeMutableProvider gtfsRealtimeProvider) {
		this.gtfsRealtimeProvider = gtfsRealtimeProvider;
	}

	/**
	 * @param refreshInterval
	 *            how often feed data will be read, in seconds.
	 */
	public void setRefreshInterval(int refreshInterval) {
		_refreshInterval = refreshInterval;
	}

	/**
	 * The start method automatically starts up a recurring task that
	 * periodically read the GTFS-realtime feed from files and refresh them.
	 */
	@PostConstruct
	public void start() {
		_log.info("starting GTFS-realtime service");
		_executor = Executors.newSingleThreadScheduledExecutor();
		_executor.scheduleAtFixedRate(new FeedRefreshTask(), 0, _refreshInterval, TimeUnit.SECONDS);
	}

	/**
	 * The stop method cancels the recurring feed data task.
	 */
	@PreDestroy
	public void stop() {
		_log.info("stopping GTFS-realtime service");
		_executor.shutdownNow();
	}

	/**
	 * Refresh feeds data from binary file
	 * 
	 * @throws IOException
	 */
	private void refreshFeed() throws IOException {

		FeedMessage tripUpdates = GtfsRealtimeLibrary.createFeedMessageBuilder().build();
		FeedMessage vehiclePositions = GtfsRealtimeLibrary.createFeedMessageBuilder().build();
		FeedMessage alerts = GtfsRealtimeLibrary.createFeedMessageBuilder().build();

		try {
			InputStream inTripUpdates = new FileInputStream(trip_update_path);
			tripUpdates = FeedMessage.parseFrom(inTripUpdates);
			_log.info("Trip Updates extracted: " + tripUpdates.getEntityCount());
			_log.debug(tripUpdates.toString());
		} catch (FileNotFoundException e) {
			_log.warn("Bad Trip Updates file");
		}

		try {
			InputStream inVehiclePositions = new FileInputStream(vehicle_positions_path);
			vehiclePositions = FeedMessage.parseFrom(inVehiclePositions);
			_log.info("Vehicle Positions extracted: " + vehiclePositions.getEntityCount());
			_log.debug(vehiclePositions.toString());
		} catch (FileNotFoundException e) {
			_log.warn("Bad Vehicle Positions file");
		}

		try {
			InputStream inAlerts = new FileInputStream(alerts_path);
			alerts = FeedMessage.parseFrom(inAlerts);

			_log.info("Alerts extracted: " + alerts.getEntityCount());
			_log.debug(alerts.toString());
		} catch (FileNotFoundException e) {
			_log.warn("Bad Alerts file");
		}

		gtfsRealtimeProvider.setTripUpdates(tripUpdates);
		gtfsRealtimeProvider.setVehiclePositions(vehiclePositions);
		gtfsRealtimeProvider.setAlerts(alerts);
	}

	/**
	 * Task that will refresh feed data from files.
	 */
	private class FeedRefreshTask implements Runnable {

		@Override
		public void run() {
			try {
				_log.info("refreshing feeds");
				refreshFeed();
			} catch (Exception ex) {
				_log.warn("Error in refresh task", ex);
			}
		}
	}

	/**
	 * @return the trip_update_path
	 */
	public String getTrip_update_path() {
		return trip_update_path;
	}

	/**
	 * @param trip_update_path
	 *            the trip_update_path to set
	 */
	public void setTrip_update_path(String trip_update_path) {
		this.trip_update_path = trip_update_path;
	}

	/**
	 * @return the vehicle_positions_path
	 */
	public String getVehicle_positions_path() {
		return vehicle_positions_path;
	}

	/**
	 * @param vehicle_positions_path
	 *            the vehicle_positions_path to set
	 */
	public void setVehicle_positions_path(String vehicle_positions_path) {
		this.vehicle_positions_path = vehicle_positions_path;
	}

	/**
	 * @return the alerts_path
	 */
	public String getAlerts_path() {
		return alerts_path;
	}

	/**
	 * @param alerts_path
	 *            the alerts_path to set
	 */
	public void setAlerts_path(String alerts_path) {
		this.alerts_path = alerts_path;
	}

}