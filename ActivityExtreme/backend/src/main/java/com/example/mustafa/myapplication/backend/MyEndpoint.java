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
import java.util.List;

import javax.inject.Named;

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

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "signup")
    public void signup(@Named("ID") String ID,@Named("name") String name,@Named("surname") String surname,
                       @Named("mail") String mail,@Named("city") String city){
        Entity e = new Entity("Users",ID);
        e.setProperty("ID",ID);
        e.setProperty("name",name);
        e.setProperty("surname",surname);
        e.setProperty("mail",mail);
        e.setProperty("city",city);
        ofy().save().entity(e).now();
    }

    @ApiMethod(name = "login")
    public Entity login(@Named("ID") String ID) {

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
                               @Named("surname")String surname,@Named("fid")String fid){
        Entity entity = new Entity("Activities");
        entity.setProperty("type",type);
        entity.setProperty("title",title);
        entity.setProperty("details",details);
        entity.setProperty("date",date);
        entity.setProperty("time",time);
        entity.setProperty("name",name);
        entity.setProperty("surname",surname);
        entity.setProperty("fid",fid);
        ofy().save().entity(entity).now();
    }

    @ApiMethod(name = "fetchWall")
    public Entity fetchWall(@Named("ID") String ID,@Named("n") int n){
        List<Entity> result = new ArrayList<>();
        Entity userInfo = login(ID);
        result.add(userInfo);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Activities");
        PreparedQuery pq = datastore.prepare(q);
        int i = 0;
        for(Entity tmp : pq.asIterable()){
            result.add(tmp);
            if(i++ == n)
                return tmp;
        }
        return null;
    }
}
