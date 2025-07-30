import Foundation
import MapKit

struct NearbyVehicleAnnotation: Identifiable {
    let id: Int
    let title: String
    let coordinate: CLLocationCoordinate2D

    init(vehicle: NearbyVehicle) {
        self.id = vehicle.id
        self.title = vehicle.vehicleModel
        self.coordinate = CLLocationCoordinate2D(latitude: vehicle.latitude, longitude: vehicle.longitude)
    }
}
