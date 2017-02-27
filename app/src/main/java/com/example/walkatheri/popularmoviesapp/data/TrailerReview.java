package com.example.walkatheri.popularmoviesapp.data;


public class TrailerReview {

    private String trailename;

    private String trailersource;
    private String trailerurl;

    public String getTrailename() {
        return trailename;
    }

    public void setTrailename(String trailename) {
        this.trailename = trailename;
    }

    public String getTrailersource() {
        return trailersource;
    }

    public void setTrailersource(String trailersource) {
        this.trailersource = trailersource;
    }

    public String getTrailerurl() {
        return trailerurl;
    }

    public void setTrailerurl(String trailerurl) {
        this.trailerurl = trailerurl;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    private String reviewAuthor;
    private String reviewContent;

     public TrailerReview(String name, String source, String url) {

        this.trailename = name;
        this.trailersource = source;
        this.trailerurl = url;


    }


    public TrailerReview(String author, String content) {
        this.reviewAuthor=author;
        this.reviewContent=content;
    }

}
