package com.deepak.moviecatalogservice.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class CatalogService
{
   @Autowired
   private RestTemplate restTemplate;

   @HystrixCommand ( fallbackMethod = "getRatingsForUserFallback" )
   public List< Rating > getRatingList( String userId )
   {
      ResponseEntity< List< Rating > > ratingList =
                                                  this.restTemplate.exchange( "http://RATINGS-DATA-SERVICE/ratings/users/"
                                                                              + userId,
                                                                              HttpMethod.GET,
                                                                              null,
                                                                              new ParameterizedTypeReference< List< Rating > >()
                                                                              {
                                                                              } );

      return ratingList != null ? ratingList.getBody() : null;
   }

   public Movie getMovie( String movieId )
   {
      ResponseEntity< Movie > movie = this.restTemplate.getForEntity( "http://MOVIE-INFO-SERVICE/movies/" + movieId,
                                                                      Movie.class );
      return movie != null ? movie.getBody() : null;
   }

   public List< Rating > getRatingsForUserFallback( String userId )
   {
      System.out.println( "Going to fallback conent" );
      return Stream.< Rating > builder()
                   .add( new Rating( "1234", 4 ) )
                   .add( new Rating( "1236", 6 ) )
                   .build()
                   .collect( Collectors.toList() );
   }

}
