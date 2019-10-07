package ClientServer.ClientRequests;

import android.graphics.Bitmap;

import com.mesibo.api.Mesibo;

import ClientServer.ServerResponse.*;
import holidays_table.ObeoHoliday;
import interests.UserInterest;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import languages.UserLanguage;
import matching_users.MatchedObeoUser;
import user.ObeoUser;

import javax.net.ssl.HttpsURLConnection;

public class ObeoClient {

    private ObeoUser clientUser;
    private static ObeoClient obeoClient;

    public static ObeoClient getInstance() {
        if (obeoClient == null) {
            obeoClient = new ObeoClient();
        }
        return obeoClient;
    }

    public ObeoUser getClientUser() {
        return clientUser;
    }


    private ObeoClient() {

    }






    public int createUser(String user_name, String first_name, String second_name,
                          java.util.Date date_of_birth, String home_city, double longitude,
                          double latitiude, String user_password, File profile, Boolean isMale, String bio) {
        try {
//            Socket socket = new Socket("localhost", 59898);
            Socket socket = new Socket("18.130.202.137", 59898);


            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Date sqlDate = new Date(date_of_birth.getTime());
            ObeoUser obeoUser = new ObeoUser(user_name, first_name, second_name, sqlDate, home_city,
                    longitude, latitiude, user_password, profile, isMale, bio);
            CreateUserRequest request = new CreateUserRequest(obeoUser);
            objectOutputStream.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            CreateUserResponse a = (CreateUserResponse) objectInputStream.readObject();


            clientUser = a.getObeoUser();

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int setupUser(String user_name, String first_name, String second_name,
                         java.util.Date date_of_birth, String user_password,
                         File profile, Boolean toHash, Boolean isMale, String bio) {
        Date sqlDate = new Date(date_of_birth.getTime());

        ObeoUser obeoUser = new ObeoUser(user_name, first_name, second_name, sqlDate, "",
                0, 0, user_password, profile, toHash, isMale, bio);

        clientUser = obeoUser;

        return 0;
    }

    public int createUserWithLocation(String home_city, double longitude, double latitiude) {
        try {
//            Socket socket = new Socket("localhost", 59898);
            Socket socket = new Socket("18.130.202.137", 59898);


            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            ObeoUser obeoUser = new ObeoUser(clientUser.getUser_name(), clientUser.getFirst_name(), clientUser.getSecond_name(), clientUser.getDate_of_birth(),
                    home_city, longitude, latitiude, clientUser.getUser_password(), null , clientUser.isMale(), clientUser.getBio());
            CreateUserRequest request = new CreateUserRequest(obeoUser);
            objectOutputStream.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            CreateUserResponse a = (CreateUserResponse) objectInputStream.readObject();


            clientUser = a.getObeoUser();

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    public int updateUserWithLocation(String home_city, double longitude, double latitude) {
        try {
//            Socket socket = new Socket("localhost", 59898);
            Socket socket = new Socket("18.130.202.137", 59898);
            String password;

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            ObeoUser obeoUser = new ObeoUser(clientUser.getId(), clientUser.getUser_name(),
                    clientUser.getFirst_name(), clientUser.getSecond_name(), clientUser.getDate_of_birth(), home_city, longitude,
                    latitude, clientUser.getUser_password(), null, clientUser.getHolidayList(), clientUser.getUserLanguages(),
                    clientUser.getUserInterests(), false, clientUser.isMale(), clientUser.getBio());
            UpdateUserRequest request = new UpdateUserRequest(obeoUser);
            objectOutputStream.writeObject(request);
            System.out.println("Sent response");

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            UpdateUserResponse a = (UpdateUserResponse) objectInputStream.readObject();

            this.clientUser = a.getObeoUser();
            System.out.println(clientUser.getFirst_name());
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    public int updateUser(String first_name, String second_name, String home_city, java.util.Date date_of_birth,
                          double longitude, double latitude, String user_password, Bitmap profile, boolean isMale,
                          String bio, boolean resetPassword) {
        try {
//            Socket socket = new Socket("localhost", 59898);
            Socket socket = new Socket("18.130.202.137", 59898);
            String password;
            if (resetPassword) {
                password = user_password;
            } else {
                password = clientUser.getUser_password(); //reset password, is now hashed password
            }

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            byte[] profileBytes = obeoClient.getClientUser().getProfile_bits();

            if (profile != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profile.compress(Bitmap.CompressFormat.PNG, 100, stream);
                profileBytes = stream.toByteArray();

            }
            System.out.println(profileBytes.length + " in le ng");

            ObeoUser obeoUser;
            if (profile == null) {
                obeoUser = new ObeoUser(clientUser.getId(), clientUser.getUser_name(),
                        first_name, second_name, new Date(date_of_birth.getTime()), home_city, longitude,
                        latitude, clientUser.getProfile_bits(), password, clientUser.getHolidayList(), clientUser.getUserLanguages(),
                        clientUser.getUserInterests(), resetPassword, isMale, bio, clientUser.getMessenger_token());
                UpdateUserRequest request = new UpdateUserRequest(obeoUser);
            } else {
                obeoUser = new ObeoUser(clientUser.getId(), clientUser.getUser_name(),
                        first_name, second_name, new Date(date_of_birth.getTime()), home_city, longitude,
                        latitude, profileBytes, password, clientUser.getHolidayList(), clientUser.getUserLanguages(),
                        clientUser.getUserInterests(), resetPassword, isMale, bio, clientUser.getMessenger_token());

            }
            UpdateUserRequest request = new UpdateUserRequest(obeoUser);
            objectOutputStream.writeObject(request);
            System.out.println("Sent response");

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            UpdateUserResponse a = (UpdateUserResponse) objectInputStream.readObject();

            this.clientUser = a.getObeoUser();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int createHoliday(java.util.Date start_date, java.util.Date end_date, String city, double longitude, double latitude, long creationTime) {
        try {
            ObeoHoliday holiday = new ObeoHoliday(city, convertUtilToSql(start_date),
                    convertUtilToSql(end_date), clientUser.getId(), longitude, latitude, creationTime);
            CreateHolidayRequest request = new CreateHolidayRequest(holiday);
//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);

            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            CreateHolidayResponse a = (CreateHolidayResponse) objectInputStream.readObject();


            clientUser.getHolidayList().add(a.getHolidayId());
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int createLogin(String user_name, String password) {
        try {
            String hashed = ObeoUser.generateHash(password);
            System.out.println(hashed);
            LoginRequest request = new LoginRequest(user_name, hashed);
//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            ServerResponse response = (ServerResponse) objectInputStream.readObject();
            if (response instanceof LoginResponse) {

                LoginResponse a = (LoginResponse) response;
                this.clientUser = a.getObeoUser();
//                try (FileOutputStream fos = new FileOutputStream("/Users/rewajshrestha/Downloads/file.jpg")) {
//                    fos.write(clientUser.getProfile_bits());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }





                return 0;
            } else {
                return -1;
            }
        } catch (UnknownHostException e) {
            return -2;
        } catch (IOException e) {
            return -2;
        } catch (ClassNotFoundException e) {
            return  -2;
        }
    }

    public ArrayList<MatchedObeoUser> lookingForLocals(ObeoHoliday obeoHoliday) {
        LookingForLocalsRequest request = new LookingForLocalsRequest(obeoHoliday);
        try {
//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            LookingForLocalsResponse response = (LookingForLocalsResponse) objectInputStream
                    .readObject();
            return (ArrayList<MatchedObeoUser>) response.getLocals();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MatchedObeoUser> lookingforTourist() {
        LookingForTouristsRequest request = new LookingForTouristsRequest(clientUser);
        try {
//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            LookingForTouristsResponse response = (LookingForTouristsResponse) objectInputStream
                    .readObject();
            System.out.println(response.getTourists() == null);
//            for (MatchedObeoUser m : response.getTourists()) {
//                try (FileOutputStream fos = new FileOutputStream("/Users/rewajshrestha/Downloads/" + m.getTourist().getUser_name() + ".jpg")) {
//
//                    fos.write(m.getTourist().getProfile_bits());
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
            return response.getTourists();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int createLanguage(String language, int proficiencyLevel) {

        try {
            UserLanguage userLanguage = new UserLanguage(clientUser.getId(), language, proficiencyLevel);
            CreateLanguageRequest request = new CreateLanguageRequest(userLanguage);
//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            CreateLanguageResponse response = (CreateLanguageResponse) objectInputStream
                    .readObject();
            clientUser.getUserLanguages().add(response.getUserLanguage());

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int updateLanguageLevel(UserLanguage userLanguage, int proficiencyLevel) {
        try {
            userLanguage.setLevel(proficiencyLevel);
            UpdateLanguageRequest request = new UpdateLanguageRequest(userLanguage);

//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            UpdateLanguageResponse response = (UpdateLanguageResponse) objectInputStream
                    .readObject();

            clientUser.getUserLanguages().remove(userLanguage);
            clientUser.getUserLanguages().add(response.getUserLanguage());
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int createInterest(String interest) {
        try {
            UserInterest userInterest = new UserInterest(clientUser.getId(), interest);
            if (!clientUser.getUserInterests().contains(userInterest)) {

                CreateInterestRequest request = new CreateInterestRequest(userInterest);

//            Socket s = new Socket("localhost", 59898);
                Socket s = new Socket("18.130.202.137", 59898);
                OutputStream o = s.getOutputStream();
                new ObjectOutputStream(o).writeObject(request);

                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

                CreateInterestResponse response = (CreateInterestResponse) objectInputStream
                        .readObject();
                clientUser.getUserInterests().add(response.getUserInterest());
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteInterest(UserInterest interest) {
        try {
            if (clientUser.getUserInterests().contains(interest)) {

                DeleteInterestRequest request = new DeleteInterestRequest(interest);
                Socket s = new Socket("18.130.202.137", 59898);

                OutputStream o = s.getOutputStream();
                new ObjectOutputStream(o).writeObject(request);

                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

                DeleteInterestResponse response = (DeleteInterestResponse) objectInputStream
                        .readObject();

                UserInterest interestToDelete = response.getUserInterest();
                ObeoClient.getInstance().getClientUser().getUserInterests().remove(interestToDelete);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int deleteLanguage(UserLanguage language) {
        try {
            DeleteLanguageRequest request = new DeleteLanguageRequest(language);
            Socket s = new Socket("18.130.202.137", 59898);

            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            DeleteLanguageResponse response = (DeleteLanguageResponse) objectInputStream
                    .readObject();

            UserLanguage languageToDelete = response.getUserLanguage();
            ObeoClient.getInstance().getClientUser().getUserLanguages().remove(languageToDelete);

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public void sendTextMessage(String to, String message) {
        Mesibo.MessageParams p = new Mesibo.MessageParams();
        p.peer = to;
        p.flag = Mesibo.FLAG_READRECEIPT | Mesibo.FLAG_DELIVERYRECEIPT;
        Mesibo.sendMessage(p, Mesibo.random(), message);
    }
    public int deleteHoliday(ObeoHoliday holiday) {
        try {
            DeleteHolidayRequest request = new DeleteHolidayRequest(holiday);
            Socket s = new Socket("18.130.202.137", 59898);

            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            DeleteHolidayResponse response = (DeleteHolidayResponse) objectInputStream
                    .readObject();

            ObeoHoliday holidayToDelete = response.getHoliday();
            ObeoClient.getInstance().getClientUser().getHolidayList().remove(holidayToDelete);

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public ObeoUser getUserFromUsername(String username) {
        try {
            GetUserFromUsernameRequest request = new GetUserFromUsernameRequest(username);

            Socket s = new Socket("18.130.202.137", 59898);

            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            GetUserFromUsernameResponse response = (GetUserFromUsernameResponse) objectInputStream.readObject();

            return response.getUser();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int matchTwoUsers(long userTwoID) {
        try {
            MatchedUserRequest request = new MatchedUserRequest(clientUser.getId(), userTwoID);

            Socket s = new Socket("18.130.202.137", 59898);

            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            MatchedUserResponse response = (MatchedUserResponse) objectInputStream.readObject();

            if (response.isMatched()) {
                return 0;
            }
            return  -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int reportUsers(long reporteeID) {
        try {
            ReportUserRequest request = new ReportUserRequest(clientUser.getId(), reporteeID);

//            Socket s = new Socket("localhost", 59898);
            Socket s = new Socket("18.130.202.137", 59898);
            OutputStream o = s.getOutputStream();
            new ObjectOutputStream(o).writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            ReportUserResponse response = (ReportUserResponse) objectInputStream.readObject();

            if (response.isBlocked()) {
                return 0;
            }
            return  -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    private static Date convertUtilToSql(java.util.Date uDate) {
        return new Date(uDate.getTime());
    }
}
