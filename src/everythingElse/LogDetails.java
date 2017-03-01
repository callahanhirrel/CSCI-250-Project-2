package everythingElse;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LogDetails {

		private StringProperty date, time, log;

		public LogDetails(String date, String time, String filename, String username,
				String comments, boolean received) {

			this.time = new SimpleStringProperty(time);
			this.date = new SimpleStringProperty(reformatDate(date));
			this.log = new SimpleStringProperty(formatLog(filename, username, comments,
					received));
		}

		private String reformatDate(String date) {
			String[] splitDate = date.split("-");
			String fixedDate = splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0].substring(2);
			return fixedDate;
		}

		private String formatLog(String filename, String username, String comments,
				boolean received) {

			String toReturn = "";

			if (received) {
				toReturn += username + " sent you the following file:\n";
			} else {
				toReturn += "You sent " + username + " the following file:\n\n";
			}

			toReturn += filename + "\n\n";

			if (!comments.equals("")) {
				toReturn += "with the following comments:\n\n" + comments;
			}

			return toReturn;
		}

		public StringProperty dateProperty() {return date;}
		public StringProperty timeProperty() {return time;}
		public StringProperty logProperty() {return log;}

}
