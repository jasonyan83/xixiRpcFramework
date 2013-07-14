package xixi.router.schedule;

import java.util.List;
import java.util.Random;

import xixi.transport.client.TcpClient;

public class WeightSelectSchedule implements RouterSchedule{

    private final Random random = new Random();
    
	@Override
	public TcpClient schedule(Short moduleId, List<TcpClient> clientList) {
		if(clientList!=null&&clientList.size()==1){
			return clientList.get(0);
		}
		else{
			 int length = clientList.size(); // 总个数
		        int totalWeight = 0; // 总权重
		        boolean sameWeight = true; // 权重是否都一样
		        for (int i = 0; i < length; i++) {
		            int weight = clientList.get(0).getWeight();
		            totalWeight += weight; // 累计总权重
		            if (sameWeight && i > 0
		                    && weight != clientList.get(i-1).getWeight()) {
		                sameWeight = false; // 计算所有权重是否一样
		            }
		        }
		        if (totalWeight > 0 && ! sameWeight) {
		            // 如果权重不相同且权重大于0则按总权重数随机
		            int offset = random.nextInt(totalWeight);
		            // 并确定随机值落在哪个片断上
		            for (int i = 0; i < length; i++) {
		                offset -= clientList.get(i).getWeight();
		                if (offset < 0) {
		                    return clientList.get(i);
		                }
		            }
		        }
		        // 如果权重相同或权重为0则均等随机
		        return clientList.get(random.nextInt(length));
		}
	}

}
