import Foundation

@objc public class PresentationAPI: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
