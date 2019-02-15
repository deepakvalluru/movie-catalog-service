package com.deepak.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.deepak.moviecatalogservice.model.CatalogItem;
import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;

@RestController
@RequestMapping ( "/catalog" )
public class CatalogController
{

   @Autowired
   private RestTemplate restTemplate;

   @RequestMapping ( method = RequestMethod.GET, path = "/{userId}" )
   public List< CatalogItem > getUserCatalog( @PathVariable ( "userId" ) String userId )
   {
      System.out.println( "Hello" );
      
      ResponseEntity< List< Rating > > ratingList =
                                                  this.restTemplate.exchange( "http://RATINGS-DATA-SERVICE/ratings/users/"
                                                                              + userId,
                                                                              HttpMethod.GET,
                                                                              null,
                                                                              new ParameterizedTypeReference< List< Rating > >()
                                                                              {
                                                                              } );
      
      
      return ratingList.getBody().stream().map( rating -> {
         Movie movie = this.restTemplate.getForEntity( "http://MOVIE-INFO-SERVICE/movies/" + rating.getMovieId(),
                                                       Movie.class )
                                        .getBody();
         return new CatalogItem( movie.getName(), movie.getDesc(), rating.getRating() );
      }
      ).collect( Collectors.toList() );
      
   }

}
