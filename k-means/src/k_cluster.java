import edu.princeton.cs.algs4.StdDraw; 

public class k_cluster 
{
    private int k, d;
    private Point[] centers;
    
    //��������������k��ά������d
    public k_cluster(int k, int d) {
        
        this.k = k;
        this.d = d;
        this.centers = new Point[k];
    }
    
    //���ݾ��ࣺa �����ά����  iteration ��������
    public void fit(double[][] a, int iteration) throws Exception
    {
        Point[] samples;
        int[] belonging; //��¼sample������
        
        //sample��
        int n_samples = a.length;
        samples = new Point[a.length];
        belonging = new int[a.length];
        
        //�������
        double[][] dist_arr = new double[n_samples][k];
        
        //check and transform
        for (int i = 0; i < n_samples; i++) {
            if (a[i].length != d)
                throw new Exception("������һ��");
            samples[i] = new Point(a[i]);
        }
        
        //�����������
        for (int i = 0; i < k; i++) { 
            int temp = (int) Math.floor(Math.random()*n_samples);
            centers[i] = samples[temp];
        }
        
        for (int i = 0; i < iteration; i++)
        {
            //k-means�������
            //����������,��������
            for (int x = 0; x < n_samples; x++)
            {
                double temp_min = Double.POSITIVE_INFINITY;
                for (int y = 0;  y < k; y++)
                {
                    dist_arr[x][y] = samples[x].distance(centers[y]); //�������
                    if (dist_arr[x][y] < temp_min) //������
                    {
                    	temp_min = dist_arr[x][y];
                        belonging[x] = y;
                    }
                }
            }
            
            double[][] new_centers_cooridinate = new double[k][d];
            for (int x = 0; x < k; x++) //������
            {    
                for (int y = 0; y < d; y++) //ά��
                {    
                    double temp_sum = 0;
                    int n_cluster_sample = 0;
                    
                    for (int z = 0; z < n_samples; z++) //�������
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
        
            //�������ģ���ȷ���Ƿ������غ�
            int temp_check = 0;
            Point[] new_centers = new Point[k];
            for (int x = 0; x < k; x++)
            {
                new_centers[x] = new Point(new_centers_cooridinate[x]);
                if (new_centers[x].equals(centers[x]))
                    temp_check++;
                centers[x] = new_centers[x];
            }
            
            //ȷ���Ƿ�����������return;
            if (temp_check == k)
                return;
            
        }	
    }
    
    //�������ݽ�����ࣺa �����ά����
    public int[] predictBatch(double[][] a) throws Exception
    {
        //��ȡһ��sample��ȷ������
        Point[] samples;
        int[] belonging; //��¼sample������
        
        //sample��
        int n_samples = a.length;
        samples = new Point[a.length];
        belonging = new int[a.length];
        
        //�������
        double[][] dist_arr = new double[n_samples][k];
        
        //check and transform
        for (int i = 0; i < n_samples; i++) {
            if (a[i].length != d)
                throw new Exception("������һ��");
            samples[i] = new Point(a[i]);
        }
        
        //����������,��������
        for (int x = 0; x < n_samples; x++)
        {
            double temp_min = Double.POSITIVE_INFINITY;
            for (int y = 0;  y < k; y++)
            {
                dist_arr[x][y] = samples[x].distance(centers[y]); //�������
                if (dist_arr[x][y] < temp_min) //������
                {
                	temp_min = dist_arr[x][y];
                    belonging[x] = y;
                }
            }
        }
        
        return belonging;
    }
    
    //����
    private class Point
    {    
        public double[] coordinate;
        
        Point(double[] temp)
        {
            this.coordinate = temp;
        }
        
        public double distance(Point y)
        {
            //�����ľ���
            double sum = 0;
            for (int i = 0; i < d; i++) {
                sum = sum + Math.pow((y.coordinate[i]-this.coordinate[i]), 2);
            }
            return sum;
        }
        
        public boolean equals(Object y)
        {
            //��ͬ����
        	
            if (y.getClass() != this.getClass())
                return false;
            
            //������غ�
            for (int i = 0; i < d; i++) {
                if (this.coordinate[i] != ((Point) y).coordinate[i])
                    return false;
            }
            return true;
        }
    }
    
    //������
    public static void main(String[] args) throws Exception {
        //���������ά�㼯����
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
        
        //����fit��predictBatch
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
        
        //��ʾ���ȷ��
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
