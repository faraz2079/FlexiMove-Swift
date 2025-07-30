import Foundation

class VehicleService: VehicleFetching {
    func fetchNearbyVehicles(address: String, completion: @escaping (Result<[NearbyVehicle], Error>) -> Void) {
        let encodedAddress = address.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? address
        guard let url = URL(string: "http://192.168.178.45:8085/vehicles/nearby?address=\(encodedAddress)&radiusInKm=3.0") else {
            print("Invalid URL")
            return
        }

        URLSession.shared.dataTask(with: url) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }

            guard let data = data else {
                completion(.failure(NSError(domain: "EmptyData", code: -1, userInfo: nil)))
                return
            }

            print("Raw JSON: \(String(data: data, encoding: .utf8) ?? "nil")")

            do {
                let decoded = try JSONDecoder().decode([NearbyVehicle].self, from: data)
                completion(.success(decoded))
            } catch {
                print("Decoding error:", error)
                completion(.failure(error))
            }
        }.resume()
    }
}
