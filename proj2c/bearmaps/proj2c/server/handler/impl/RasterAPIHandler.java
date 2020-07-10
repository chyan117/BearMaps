package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.SEMANTIC_STREET_GRAPH;
import static bearmaps.proj2c.utils.Constants.ROUTE_LIST;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        Map<String, Object> results = new HashMap<>();
        double ullong_bound = -122.2998046875;
        double ullat_bound = 37.892195547244356;
        double lrlong_bound = -122.2119140625;
        double lrlat_bound = 37.82280243352756;
        double long_bound_minus = lrlong_bound- ullong_bound;
        double lat_bound_munus = ullat_bound - lrlat_bound;
        //the parameter value given by the spec
        double ullong = requestParams.get("ullon");
        double ullat = requestParams.get("ullat");
        double lrlong = requestParams.get("lrlon");
        double lrlat = requestParams.get("lrlat");
        /*
        if(ullong<ullong_bound){
            ullong= ullong_bound;
        }
        if(lrlong>lrlong_bound){
            lrlong = lrlong_bound;
        }
        if(ullat>ullat_bound){
            ullat= ullat_bound;
        }
        if(lrlat<lrlat_bound){
            lrlat = lrlat_bound;
        }
        */
        double width = requestParams.get("w");
        double hight = requestParams.get("h");
        boolean query_success = check(ullong_bound, ullat_bound, lrlong_bound, lrlat_bound, ullong, lrlong, ullat, lrlat);
        results.put("query_success", query_success);
        //Motto
        double Input_LonDPP = Input_LonDPP(lrlong, ullong, width);
        //The return array must have LonDPP less than or equal to above
        int depth = Compute_depth(Input_LonDPP);
        results.put("depth", depth);
        System.out.println(depth);
        //total column in the pic
        int col_total = (int) Math.pow(2, depth);

        double distance_per_array_lon = (lrlong_bound-ullong_bound)/col_total;
        double distance_per_array_lat = (ullat_bound-lrlat_bound)/col_total;

        int started_long=  (int)(( ullong - ullong_bound )/distance_per_array_lon);

        int started_lat=  (int)((ullat_bound- ullat)/distance_per_array_lat);

        int ended_long=  (int)((lrlong - ullong_bound)/distance_per_array_lon);

        int ended_lat=  (int)( (ullat_bound-lrlat )/distance_per_array_lat);
        int x = ended_lat-started_lat+1;
        if(x>col_total){
            x = col_total;
        }
        int y = ended_long- started_long+1;
        if(y>col_total){
            y = col_total;
        }
        String[][] render_grid = new String[x][y];
        for(int i=0; i<(ended_lat-started_lat)+1;i++ ){
            for(int j=0; j<(ended_long-started_long)+1; j++){
                if(started_long+j>=col_total||started_lat+i>=col_total){
                    continue;
                }
                String s = "d"+depth+"_x"+(started_long+j)+"_y"+(started_lat+i)+".png";
                render_grid[i][j]=s;
            }
        }

        results.put("render_grid", render_grid);
        //rastering raster_ul_lon
        results.put("raster_ul_lon", ullong_bound+(long_bound_minus/col_total)*started_long );
        //rastering raster_lr_lon
        results.put("raster_lr_lon", ullong_bound+(long_bound_minus/col_total)*(ended_long+1) );
        //rastering raster_ul_lat
        results.put("raster_ul_lat", ullat_bound-(lat_bound_munus/col_total)*started_lat);
        //rastering raster_lr_lat
        results.put("raster_lr_lat", ullat_bound-(lat_bound_munus/col_total)*(ended_lat+1));
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        return results;
    }
    private boolean check(double bound_ul_long, double bound_ul_lat, double bound_ll_long, double bound_ll_lat,
                          double ul_long, double ll_long, double ul_lat, double ll_lat){
        /*
        if(ul_long<bound_ul_long){
            System.out.println("ul_long is lefter than bound! ");
            return false;
        }
        if(ll_long>bound_ll_long){
            System.out.println("ll_long is righter than bound! ");
            return false;
        }
        if(ul_lat>bound_ul_lat){
            System.out.println("ul_lat is higher than bound! ");
            return false;
        }
        if(ll_lat<bound_ll_lat){
            System.out.println("ll_long is lower than bound! ");
            return false;
        }

         */
        if(ul_long>=ll_long){
            System.out.println("ul_long is righter than ll_long! ");
            return false;
        }
        if(ul_lat<=ll_lat){
            System.out.println("ul_lat is lower than ll_lat! ");
            return false;
        }
        return true;
    }
    private double Input_LonDPP(double lrlong, double ullong, double width){
        return (lrlong-ullong)/width;
    }
    //the parameter LonDPP is the required LonDPP
    private int Compute_depth(double LonDPP){
        //The original
        double scale_0  = (  -122.21191406 - (-122.29980468)  )   /256;
        //the depth is the original/x <= LonDPP
        double Base_num = scale_0/LonDPP;
        double x = (Math.log(Base_num) / Math.log(2) );
        if(x>=7.0){
            return 7;
        }
        return (int)Math.ceil(x);
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
