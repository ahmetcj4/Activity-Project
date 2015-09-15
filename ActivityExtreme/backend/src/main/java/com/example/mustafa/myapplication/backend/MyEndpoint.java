/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.mustafa.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Filter;

import javax.inject.Named;
import javax.xml.crypto.Data;

import sun.rmi.runtime.Log;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.mustafa.example.com",
                ownerName = "backend.myapplication.mustafa.example.com",
                packagePath = ""
        )
)

public class MyEndpoint {

    @ApiMethod(name = "signup")
    public void signup(@Named("ID") String ID,@Named("name") String name,@Named("surname") String surname,
                       @Named("ppUrl") String ppUrl,@Named("location") String location){
        Entity e = new Entity("Users",ID);
        e.setProperty("ID",ID);
        e.setProperty("name",name);
        e.setProperty("surname",surname);
        e.setProperty("ppUrl",ppUrl);
        e.setProperty("location",location);
        ofy().save().entity(e).now();
    }

    @ApiMethod(name = "getUserInformation")
    public Entity getUserInformation(@Named("ID") String ID) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter nameFilter =
                new Query.FilterPredicate("ID",
                        Query.FilterOperator.EQUAL,
                        ID);
        Query q = new Query("Users").setFilter(nameFilter);
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            return result;
        }
        return  null;
    }

    @ApiMethod(name = "createActivity")
    public void createActivity(@Named("type") String type,@Named("title") String title,@Named("details") String details,
                               @Named("date") String date,@Named("time")String time,@Named("name")String name,
                               @Named("surname")String surname,@Named("fid")String fid,@Named("ppUrl") String ppUrl,
                               @Named("location") String location){
        Entity entity = new Entity("Activities");
        entity.setProperty("id",fid+"_"+date+"_"+time);
        entity.setProperty("type",type);
        entity.setProperty("title",title);
        entity.setProperty("details",details);
        entity.setProperty("date",date);
        entity.setProperty("time",time);
        entity.setProperty("name",name);
        entity.setProperty("surname",surname);
        entity.setProperty("fid",fid);
        entity.setProperty("ppUrl",ppUrl);
        entity.setProperty("location",location);
        ofy().save().entity(entity).now();
    }

    @ApiMethod(name = "fetchWall")
    public List<Entity> fetchWall(){
        List<Entity> list = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Activities")
                .addSort("date", Query.SortDirection.ASCENDING);
        PreparedQuery pq = datastoreService.prepare(q);
        for(Entity tmp : pq.asIterable())
            list.add(tmp);
        return list;
    }

    @ApiMethod(name="commentUser")
    public void commentUser(@Named("ID") String id,@Named("commenterID") String commenterID,
                            @Named("comment") String comment,@Named("ppUrl") String ppUrl,
                            @Named("location") String location,@Named("date") String date,
                            @Named("time")String time,@Named("name")String name,
                            @Named("surname")String surname){
        Entity entity = new Entity("commentUser" + id);
        entity.setProperty("comment",comment);
        entity.setProperty("commenterID",commenterID);
        entity.setProperty("ppUrl",ppUrl);
        entity.setProperty("location",location);
        entity.setProperty("date",date);
        entity.setProperty("time",time);
        entity.setProperty("name",name);
        entity.setProperty("surname",surname);
        ofy().save().entity(entity).now();
    }
    @ApiMethod(name="getCommentsUser")
    public List<Entity> getCommentsUser(@Named("ID") String id){
        List<Entity> result = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("commentUser"+id);
        PreparedQuery pq = datastoreService.prepare(query);
        int i=0;
        for(Entity tmp : pq.asIterable())
                result.add(tmp);
        return result;
    }

    // An activity's id is the concatenation of creator's id, "_" and create time.
    @ApiMethod(name="commentActivity") // Daha tam degil
    public void commentActivity(@Named("fid")String fid,@Named("dateTime")String dateTime
            ,@Named("commenterID") String commenterID,@Named("comment")String comment){
        Entity entity = new Entity("activityComment" + fid + '_' + dateTime);
        entity.setProperty("fid",fid);
        entity.setProperty("commenterID",commenterID);
        entity.setProperty("comment", comment);
        Entity x = getUserInformation(fid);
        entity.setProperty("ppUrl",x.getProperty("ppUrl"));
        entity.setProperty("name",x.getProperty("name"));
        entity.setProperty("surname",x.getProperty("surname"));
        ofy().save().entity(entity).now();
    }

    @ApiMethod(name="getCommentsActivity")
    public List<Entity> getCommentsActivity(@Named("fid")String fid,@Named("dateTime") String dateTime){
        List<Entity> result = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("activityComment" + fid + "_" + dateTime);
        PreparedQuery pq = datastoreService.prepare(query);
        for(Entity e : pq.asIterable())
            result.add(e);
        return result;
    }

    @ApiMethod(name="isLiked")
    public Entity isLiked(@Named("fid")String fid,@Named("dateTime")String dateTime,
                           @Named("userId")String userId){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter nameFilter =
                new Query.FilterPredicate("userId",
                        Query.FilterOperator.EQUAL,
                        userId);
        Query q = new Query("likes_" + fid + "_" + dateTime)
                .setFilter(nameFilter);
        PreparedQuery pq = datastoreService.prepare(q);
        for(Entity e: pq.asIterable())
            return e;
        return null;
    }

    // fid is id of creator of activity, dateTime is date + time of activity.
    @ApiMethod(name="likeUnlikeActivity")
    public void likeUnlikeActivity(@Named("fid")String fid,@Named("dateTime")String dateTime
            ,@Named("userId")String userId){
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("likes_" + fid + "_" + dateTime,fid+"_"+dateTime);
        entity.setProperty("userId",userId);
        Entity res = isLiked(fid, dateTime, userId);
        if(res == null)
            dataStore.put(entity);
        else
            dataStore.delete(res.getKey());
    }

    @ApiMethod(name="isLikedPerson")
    public Entity isLikedPerson(@Named("fid")String fid,@Named("userId")String userId){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter nameFilter =
                new Query.FilterPredicate("userId",
                        Query.FilterOperator.EQUAL,
                        userId);
        Query q = new Query("likes_" + fid)
                .setFilter(nameFilter);
        PreparedQuery pq = datastoreService.prepare(q);
        for(Entity e: pq.asIterable())
            return e;
        return null;
    }

    // fid is id of creator of activity, dateTime is date + time of activity.
    @ApiMethod(name="likeUnlikePerson")
    public void likeUnlikePerson(@Named("fid")String fid,@Named("userId")String userId){
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("likes_" + fid,fid);
        entity.setProperty("userId",userId);
        Entity res = isLikedPerson(fid, userId);
        if(res == null)
            dataStore.put(entity);
        else
            dataStore.delete(res.getKey());
    }

    // It is not working now.
    @ApiMethod(name="getLikesPerson",path = "getLikesPerson")
    public List<Entity> getLikesPerson(@Named("fid") String fid){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("likes_" + fid);
        PreparedQuery pq = datastoreService.prepare(q);
        List<Entity> res = new ArrayList<>();
        for(Entity e:pq.asIterable())
            res.add(e);
        return res;
    }
    // It is not working now.
    @ApiMethod(name="getLikesActivity",path = "getLikes")
    public List<Entity> getLikesActivity(@Named("fid") String fid,@Named("dateTime")String dateTime){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("likes_" + fid + "_" + dateTime);
        PreparedQuery pq = datastoreService.prepare(q);
        List<Entity> res = new ArrayList<>();
        for(Entity e:pq.asIterable())
            res.add(e);
        return res;
    }
    @ApiMethod(name="attendActivity",path="attendActivity")
    public void attendActivity(@Named("fid")String fid,@Named("activityID") String activityId){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity e = new Entity("attend_"+fid);
        e.setProperty("activityID",activityId);
        datastore.put(e);

        e = new Entity("whoAttends_"+activityId);
        e.setProperty("fid",fid);
        datastore.put(e);
    }

    @ApiMethod(name="whoAttends",path = "whoAttends")
    public List<Entity> whoAttends(@Named("activityID") String activityID){
        List<Entity> res = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("whoAttends_"+activityID);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity e : pq.asIterable()){
            Entity x = getUserInformation((String) e.getProperty("fid"));
            res.add(x);
        }
        return res;
    }

    @ApiMethod(name="getAttendedActivities",path="getAttendedActivities")
    public List<Entity> getAttendedActivities(@Named("fid") String fid){
        Calendar c = Calendar.getInstance();
        String sMonth = (c.get(Calendar.MONTH)+1<10?"0":"") + (c.get(Calendar.MONTH)+1);
        String sDayOfMonth = (c.get(Calendar.DAY_OF_MONTH)<10?"0":"") + c.get(Calendar.DAY_OF_MONTH);
        String sHourOfDay = (c.get(Calendar.HOUR_OF_DAY)<10?"0":"") + c.get(Calendar.HOUR_OF_DAY);
        String sMinute = (c.get(Calendar.MINUTE)<10?"0":"") + c.get(Calendar.MINUTE);
        String sDate = c.get(Calendar.YEAR) + "." + sMonth
                + "." + sDayOfMonth ;
        String sTime = sHourOfDay + ":" + sMinute;
        List<Entity> res = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("attend_"+fid);
        PreparedQuery pq = datastoreService.prepare(q);
        for(Entity e: pq.asIterable()){
            Entity x = getActivity((String) e.getProperty("activityID"));
            String s =(String)x.getProperty("date")+(String)x.getProperty("time");
            if(s.compareTo(sDate+sTime)<0)
                res.add(x);
        }
        return res;
    }

    @ApiMethod(name="getActivity",path="getActivity")
    public Entity getActivity(@Named("id")String  id){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL,id);
        Query q = new Query("Activities").setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity e: pq.asIterable())
            return e;
        return null;
    }

    @ApiMethod(name="getOncomingActivities",path="getOncomingActivities")
    public List<Entity> getOncomingActivities(@Named("fid") String fid){
        Calendar c = Calendar.getInstance();
        String sMonth = (c.get(Calendar.MONTH)+1<10?"0":"") + (c.get(Calendar.MONTH)+1);
        String sDayOfMonth = (c.get(Calendar.DAY_OF_MONTH)<10?"0":"") + c.get(Calendar.DAY_OF_MONTH);
        String sHourOfDay = (c.get(Calendar.HOUR_OF_DAY)<10?"0":"") + c.get(Calendar.HOUR_OF_DAY);
        String sMinute = (c.get(Calendar.MINUTE)<10?"0":"") + c.get(Calendar.MINUTE);
        String sDate = c.get(Calendar.YEAR) + "." + sMonth
                + "." + sDayOfMonth ;
        String sTime = sHourOfDay + ":" + sMinute;
        List<Entity> res = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("attend_"+fid);
        PreparedQuery pq = datastoreService.prepare(q);
        for(Entity e: pq.asIterable()){
            Entity x = getActivity((String) e.getProperty("activityID"));
            if(((String)x.getProperty("date")+(String)x.getProperty("time")).compareTo(sDate+sTime)>0)
                res.add(x);
        }
        return res;
    }

}
