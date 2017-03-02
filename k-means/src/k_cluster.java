import edu.princeton.cs.algs4.StdDraw; 

public class k_cluster 
{
    private int k, d;
    private Point[] centers;
    
    //建立：聚类数量k，维度数量d
    public k_cluster(int k, int d) {
        
        this.k = k;
        this.d = d;
        this.centers = new Point[k];
    }
    
    //数据聚类：a 输入二维数组  iteration 迭代次数
    public void fit(double[][] a, int iteration) throws Exception
    {
        Point[] samples;
        int[] belonging; //记录sample所属类
        
        //sample数
        int n_samples = a.length;
        samples = new Point[a.length];
        belonging = new int[a.length];
        
        //距离矩阵
        double[][] dist_arr = new double[n_samples][k];
        
        //check and transform
        for (int i = 0; i < n_samples; i++) {
            if (a[i].length != d)
                throw new Exception("列数不一致");
            samples[i] = new Point(a[i]);
        }
        
        //生成随机中心
        for (int i = 0; i < k; i++) { 
            int temp = (int) Math.floor(Math.random()*n_samples);
            centers[i] = samples[temp];
        }
        
        for (int i = 0; i < iteration; i++)
        {
            //k-means主体过程
            //计算距离矩阵,属类数组
            for (int x = 0; x < n_samples; x++)
            {
                double temp_min = Double.POSITIVE_INFINITY;
                for (int y = 0;  y < k; y++)
                {
                    dist_arr[x][y] = samples[x].distance(centers[y]); //计算距离
                    if (dist_arr[x][y] < temp_min) //配属类
                    {
                    	temp_min = dist_arr[x][y];
                        belonging[x] = y;
                    }
                }
            }
            
            double[][] new_centers_cooridinate = new double[k][d];
            for (int x = 0; x < k; x++) //聚类编号
            {    
                for (int y = 0; y < d; y++) //维度
                {    
                    double temp_sum = 0;
                    int n_cluster_sample = 0;
                    
                    for (int z = 0; z < n_samples; z++) //样本编号
                    {	
                        if (belonging[z] == x)
                        {
                            temp_sum = temp_sum + samples[z].coordinate[y];
                            n_cluster_sample++;
                        }
                    }
                    
                    if (n_cluster_sample == 0)
                    	new_centers_cooridinate[x][y] = centers[x].coordinate[y];
                    else
                    	new_centers_cooridinate[x][y] = temp_sum/n_cluster_sample;
                }
            }
        
            //更新中心，并确认是否中心重合
            int temp_check = 0;
            Point[] new_centers = new Point[k];
            for (int x = 0; x < k; x++)
            {
                new_centers[x] = new Point(new_centers_cooridinate[x]);
                if (new_centers[x].equals(centers[x]))
                    temp_check++;
                centers[x] = new_centers[x];
            }
            
            //确认是否收敛，收敛return;
            if (temp_check == k)
                return;
            
        }	
    }
    
    //聚类后根据结果分类：a 输入二维数组
    public int[] predictBatch(double[][] a) throws Exception
    {
        //读取一组sample并确认属类
        Point[] samples;
        int[] belonging; //记录sample所属类
        
        //sample数
        int n_samples = a.length;
        samples = new Point[a.length];
        belonging = new int[a.length];
        
        //距离矩阵
        double[][] dist_arr = new double[n_samples][k];
        
        //check and transform
        for (int i = 0; i < n_samples; i++) {
            if (a[i].length != d)
                throw new Exception("列数不一致");
            samples[i] = new Point(a[i]);
        }
        
        //计算距离矩阵,属类数组
        for (int x = 0; x < n_samples; x++)
        {
            double temp_min = Double.POSITIVE_INFINITY;
            for (int y = 0;  y < k; y++)
            {
                dist_arr[x][y] = samples[x].distance(centers[y]); //计算距离
                if (dist_arr[x][y] < temp_min) //配属类
                {
                	temp_min = dist_arr[x][y];
                    belonging[x] = y;
                }
            }
        }
        
        return belonging;
    }
    
    //点类
    private class Point
    {    
        public double[] coordinate;
        
        Point(double[] temp)
        {
            this.coordinate = temp;
        }
        
        public double distance(Point y)
        {
            //点与点的距离
            double sum = 0;
            for (int i = 0; i < d; i++) {
                sum = sum + Math.pow((y.coordinate[i]-this.coordinate[i]), 2);
            }
            return sum;
        }
        
        public boolean equals(Object y)
        {
            //相同类型
        	
            if (y.getClass() != this.getClass())
                return false;
            
            //定义点重合
            for (int i = 0; i < d; i++) {
                if (this.coordinate[i] != ((Point) y).coordinate[i])
                    return false;
            }
            return true;
        }
    }
    
    //测试用
    public static void main(String[] args) throws Exception {
        //生成随机二维点集数组
        int n = 300;
        int d = 2;
        int iteration = 100;
        int k = 5;
        
        double[][] a = new double[n][d];
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < d; j++){
                a[i][j] = (double) Math.random();
                //System.out.print(a[i][j]);
                //System.out.print(' ');
            }
            //System.out.println();
        }
        
        //调用fit与predictBatch
        k_cluster k_test = new k_cluster(k, d); 
        k_test.fit(a, iteration); 
        int[] type = k_test.predictBatch(a);
        
        //for (int i = 0; i < n; i++)
        	//System.out.println(type[i]);
        /*
        for (int i = 0; i < k; i++) { 
            for (int j = 0; j < d; j++){
                System.out.print(k_test.centers[i].coordinate[j]);
                System.out.print(' ');	
            }
            System.out.println();
        }
        */
        
        //显示输出确认
        StdDraw.setPenRadius(0.01);
        for (int i = 0; i < n; i++)
        {
        	switch(type[i]){
        	case 0:StdDraw.setPenColor(StdDraw.RED);break;
        	case 1:StdDraw.setPenColor(StdDraw.BLACK);break;
        	case 2:StdDraw.setPenColor(StdDraw.GREEN);break;
        	case 3:StdDraw.setPenColor(StdDraw.BLUE);break;
        	case 4:StdDraw.setPenColor(StdDraw.YELLOW);
        	}
        	StdDraw.point(a[i][0], a[i][1]);
        }
        
        
        
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(k_test.centers[0].coordinate[0], k_test.centers[0].coordinate[1]);
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.point(k_test.centers[1].coordinate[0], k_test.centers[1].coordinate[1]);
    	StdDraw.setPenColor(StdDraw.GREEN);
    	StdDraw.point(k_test.centers[2].coordinate[0], k_test.centers[2].coordinate[1]);
    	StdDraw.setPenColor(StdDraw.BLUE);
    	StdDraw.point(k_test.centers[3].coordinate[0], k_test.centers[3].coordinate[1]);
    	StdDraw.setPenColor(StdDraw.YELLOW);
    	StdDraw.point(k_test.centers[4].coordinate[0], k_test.centers[4].coordinate[1]);
		
    }
    

}
