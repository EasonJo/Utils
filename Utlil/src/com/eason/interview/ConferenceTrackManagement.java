package com.eason.interview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ThoughtWorks 面试题,日程管理.
 * 运行需要提供一个测试文件的File.例如Main方法中注释所写.
 *
 * @author <a href="mailto:pujl1@lenovo.com">Eason Pu</a>
 * @date 1/20/15
 */
public class ConferenceTrackManagement {

    private final int AM_TIME = 180;//total min of AM
    private final int PM_TIME = 240; //total min of PM
    private final int MAX_TIME = 420; // 7 * 60 min, Working Time
    private Calendar cal = new GregorianCalendar();

    public static void main(String[] args) {
        new ConferenceTrackManagement().process();

        /*Test Input data*/
        /*Writing Fast Tests Against Enterprise Rails 60min
        Overdoing it in Python 45min
        Lua for the Masses 30min
        Ruby Errors from Mismatched Gem Versions 45min
        Common Ruby Errors 45min
        Rails for Python Developers lightning
        Communicating Over Distance 60min
        Accounting-Driven Development 45min
        Woah 30min
        Sit Down and Write 30min
        Pair Programming vs Noise 45min
        Rails Magic 60min
        Ruby on Rails: Why We Should Move On 60min
        Clojure Ate Scala (on my project) 45min
        Programming in the Boondocks of Seattle 30min
        Ruby vs. Clojure for Back-End Development 30min
        Ruby on Rails Legacy App Maintenance 60min
        A World Without HackerNews 30min
        User Interface CSS in Rails Apps 30min
        */

        /*OUT PUT*/
        /*
        Track: 1
        09:00PM Lua for the Masses
        09:30PM Woah
        10:00PM Sit Down and Write
        10:30PM Programming in the Boondocks of Seattle
        11:00PM Ruby vs. Clojure for Back-End Development
        11:30PM A World Without HackerNews

        12:00PM Lunch
        01:00AM Rails for Python Developers
        01:05AM User Interface CSS in Rails Apps
        01:35AM Overdoing it in Python
        02:20AM Ruby Errors from Mismatched Gem Versions
        03:05AM Common Ruby Errors
        03:50AM Accounting-Driven Development
        04:35AM NetWorking Event

        Track: 2
        09:00AM Writing Fast Tests Against Enterprise Rails
        10:00AM Communicating Over Distance
        11:00AM Rails Magic

        12:00PM Lunch
        01:00PM Pair Programming vs Noise
        01:45PM Clojure Ate Scala (on my project)
        02:30PM Ruby on Rails: Why We Should Move On
        03:30PM Ruby on Rails Legacy App Maintenance
        04:30PM NetWorking Event
        */

    }

    /**
     * load the Conference from file
     *
     * @param file
     * @return
     */
    private List<Conference> loadInputData(File file) {
        if (file == null || !file.exists()) {
            System.out.println("test file is not exist");
            return null;
        }
        ArrayList<Conference> data = new ArrayList<Conference>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                int lastIndexOfSpace = line.lastIndexOf(" ");
                String desc = line.substring(0, lastIndexOfSpace);
                String timeStr = line.substring(lastIndexOfSpace + 1);

                if (!"".equals(desc) && !"".equals(timeStr)) {
                    Conference conference = new Conference();
                    conference.setDesc(desc);
                    conference.setTime(getTimeValue(timeStr));

                    data.add(conference);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * sort the Conference by time
     *
     * @param list
     */
    private void sortConference(List<Conference> list) {
        if (list == null || list.size() <= 1) {
            return;
        }

        Collections.sort(list, new Comparator<Conference>() {
            @Override
            public int compare(Conference o1, Conference o2) {
                if (o1.getTime() == o2.getTime()) {
                    return 0;
                } else if (o1.getTime() > o2.getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

    }

    /**
     * get Time Value form String
     *
     * @param timeStr time String
     * @return int value
     */
    private int getTimeValue(String timeStr) {
        int time = 0;
        timeStr = timeStr.toLowerCase();
        if (timeStr.endsWith("min")) {
            time = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("min")));
        } else if (timeStr.endsWith("lightning")) {
            time = 5;
        }

        return time;
    }

    /**
     * To compute a part of Conferences which whole time is num
     *
     * @param cfs
     * @param num
     */
    private List<Conference> computeConferences(List<Conference> cfs, int num) {
        if (getTotalTime(cfs) < num) {
            return cfs;
        }

        ArrayList<Conference> c = new ArrayList<Conference>();
        int currentSum = cfs.get(0).getTime();
        int start = 0;

        for (int i = 0; i < cfs.size(); i++) {

            while (currentSum > num && start < i) {
                currentSum -= cfs.get(start).getTime();
                start++;
            }

            if (currentSum == num) {
                for (int j = start; j <= i; j++) {
                    Conference cc = cfs.get(j);
                    c.add(cc);
                }
                return c;
            }

            if (i < cfs.size() - 1) {
                currentSum += cfs.get(i + 1).getTime();
            }
        }

        return c;
    }

    /**
     * To compute a part of Conferences which whole Time is between min and max
     *
     * @param cfs Conferences
     * @param min
     * @param max
     * @return
     */
    private List<Conference> computeConferences(List<Conference> cfs, int min, int max) {
        if (getTotalTime(cfs) < max) {
            return cfs;
        }
        ArrayList<Conference> c = new ArrayList<Conference>();

        int currentSum = cfs.get(0).getTime();
        int start = 0;

        for (int i = 0; i < cfs.size(); i++) {

            while (currentSum > max && start < i) {
                currentSum -= cfs.get(start).getTime();
                start++;
            }

            if (currentSum >= min && currentSum <= max) {
                for (int j = start; j <= i; j++) {
                    Conference cc = cfs.get(j);
                    c.add(cc);
                }
                return c;
            }

            if (i < cfs.size() - 1) {
                currentSum += cfs.get(i + 1).getTime();
            }
        }

        return c;
    }

    /**
     * format date
     *
     * @return string of timestamp
     */
    private String timeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("h:mma");
        String ss = df.format(cal.getTime());
        int h = cal.get(Calendar.HOUR);
        if (h < 10) {
            ss = "0" + ss;
        }
        return ss;
    }

    /**
     * get the Total Time of Conferences
     *
     * @param Conferences All Conferences
     * @return total time
     */
    private int getTotalTime(List<Conference> Conferences) {
        int value = 0;
        for (Conference c : Conferences) {
            value += c.getTime();
        }
        return value;
    }

    /**
     * reset everyday date,start from 9:00AM
     */
    private void resetDate() {
        if (cal != null) {
            cal.set(Calendar.HOUR, 9);
            cal.set(Calendar.MINUTE, 0);
        }
    }

    /**
     * process the Conference
     */
    public void process() {

        //firstly, load the test data.

        //TODO, need to change this File path to you file path
        List<Conference> list = loadInputData(new File("/home/pujl/Android_Demo_Source/ConferenceTrackManagement/src/test.txt"));
        if (list == null || list.size() == 0) {
            System.out.println("Test data is Null");
            return;
        }
        sortConference(list);

        int totalTime = getTotalTime(list);

        int tracks;
        if (totalTime % MAX_TIME == 0) {
            tracks = totalTime / MAX_TIME;
        } else {
            tracks = totalTime / MAX_TIME + 1;
        }

        //handle Conference
        for (int i = 0; i < tracks; i++) {
            resetDate();
            //get AM Conferences
            List<Conference> am_c = computeConferences(list, AM_TIME);

            System.out.println("Track: " + (i + 1));
            for (Conference c : am_c) {
                //String str = timeStamp() + " " + c.getDesc() + " " + c.getTime() + "min";
                String str = timeStamp() + " " + c.getDesc();
                cal.add(Calendar.MINUTE, c.getTime());
                System.out.println(str);
            }
            System.out.println("");
            System.out.println("12:00PM Lunch");
            cal.add(Calendar.MINUTE, 60);

            //Removed Planed AM Conference
            list.removeAll(am_c);
            //get PM Conferences
            List<Conference> pm_c = computeConferences(list, AM_TIME, PM_TIME);

            for (Conference c : pm_c) {
                //String str = timeStamp() + " " + c.getDesc() + " " + c.getTime() + "min";
                String str = timeStamp() + " " + c.getDesc();
                cal.add(Calendar.MINUTE, c.getTime());
                System.out.println(str);
            }
            System.out.println(timeStamp() + " NetWorking Event\n");

            //remove planed PM conference
            list.removeAll(pm_c);
        }

    }

    private final class Conference {
        private String desc;
        private int time;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
