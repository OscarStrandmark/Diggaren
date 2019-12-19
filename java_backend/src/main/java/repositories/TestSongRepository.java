package repositories;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.TestSong;

@Path("/testSong")
public class TestSongRepository {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String info() {
		return "welcome to my test-page";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeSong(TestSong song) {
		String res = "received song object:\n"+song;
		return Response.status(201).entity(res).build();
	}
	
	
}
