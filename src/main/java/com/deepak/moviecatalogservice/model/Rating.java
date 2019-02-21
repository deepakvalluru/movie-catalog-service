package com.deepak.moviecatalogservice.model;

public class Rating
{
   private String movieId;
   private int rating;
   
   public Rating()
   {
      
   }
   
   public Rating( String movieId, int rating )
   {
      super();
      this.movieId = movieId;
      this.rating = rating;
   }
   /**
    * @return the movieId
    */
   public String getMovieId()
   {
      return movieId;
   }
   /**
    * @param movieId the movieId to set
    */
   public void setMovieId( String movieId )
   {
      this.movieId = movieId;
   }
   /**
    * @return the rating
    */
   public int getRating()
   {
      return rating;
   }
   /**
    * @param rating the rating to set
    */
   public void setRating( int rating )
   {
      this.rating = rating;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "Rating [movieId=" + movieId + ", rating=" + rating + "]";
   }
}
