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
import java.util.Comparator;
import java.util.List;
import java.util.logging.Filter;

import javax.inject.Named;
import javax.xml.crypto.Data;

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
                            @Named("comment") String comment){
        Entity entity = new Entity("commentUser" + id);
        entity.setProperty("comment",comment);
        entity.setProperty("commenterID",commenterID);
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
        entity.setProperty("comment",comment);
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

    @ApiMethod(name="getLikes")
    public List<Entity> a(@Named("fid") String fid, @Named("dateTime") String dateTime){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("likes_" + fid + "_" + dateTime);
        PreparedQuery pq = datastoreService.prepare(q);
        List<Entity> res = new ArrayList<>();
        for(Entity e:pq.asIterable())
            res.add(e);
        return res;
    }

}
