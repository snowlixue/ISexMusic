#include<iostream>
#include<cstdlib>
#include<ctime>
#include<ctime>
using namespace  std;


double random(double start, double end)
{
    return start+(end-start)*rand()/(RAND_MAX );
}

   int main()
   {
      clock_t time1,time2;



      int num[5000]={0};

      int temp,max,k;

//产生随机数

        double random(double,double);

        srand(unsigned(time(0)));

    for(int icnt = 0; icnt != 5000; ++icnt)
        num[icnt]=(random(0,5000));

//开始计时。
         time1 = clock();

//冒泡法
      for(int j=0;j<5000;j++)
      {

        for(k=0; k<5000; k++)
      {
          if(num[j]<=num[k])
          {
              temp=num[j];
              num[j]=num[k];
              num[k]=temp;
          }

      }
      }

      time2 = clock() ;



      for(int l=0;l<5000;l++)

        cout<<num[l]<<" ";





 /*插入

         int list[5000]={0};

               list[0]=num[0];

               list[1]=num[1];

       for(int j=2;j<5000;j++)
       {
           for(k=j-1;k>=0;k--)
           {
               if(num[j]<list[k])
               {
                   for(j;j>k;j--)
                    list[j]=list[j-1];

                 list[k]=num[j];
               }
               break;
           }

       }
        time2 = clock() ;


       for(int l=0;l<5000;l++)

        cout<<list[l]<<" ";

*/


       cout<<"\n排序用时："<< time2-time1 << "ms" <<endl;

   }



