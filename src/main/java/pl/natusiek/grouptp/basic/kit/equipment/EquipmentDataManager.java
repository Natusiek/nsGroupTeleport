package pl.natusiek.grouptp.basic.kit.equipment;

import java.util.HashMap;
import java.util.Map;

public class EquipmentDataManager {

    private final Map<String, EquipmentData> equipments = new HashMap<>();

    public Map<String, EquipmentData> getEquipments() {
        return new HashMap<>(equipments);
    }

    public EquipmentData createEquipmentData(String name) {
        EquipmentData data = this.equipments.get(name);
        if (data == null) {
            this.equipments.put(name, data = new EquipmentData());
        }
        return data;
    }

}
